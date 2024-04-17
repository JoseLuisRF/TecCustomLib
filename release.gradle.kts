import java.net.URI

fun Project.getExtra(extraKey: String, tryRoot: Boolean = true): String? =
    runCatching { extra[extraKey]!!.toString() }.getOrNull() ?: if (tryRoot) {
        rootProject.getExtra(extraKey, tryRoot = false)
    } else {
        null
    }

val BUILD_VERSION = System.getenv("BUILD_VERSION") ?: "0.0.5-beta"
extra["artifactVersion"] = BUILD_VERSION
extra["artifactGroup"] = System.getenv("ARTIFACT_GROUP") ?: "com.jlrf.tec"

subprojects {
    apply(plugin = "maven-publish")

    afterEvaluate {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("artifact") {
                    groupId = getExtra("artifactGroup")
                    artifactId = getExtra("artifactId") ?: this@subprojects.name
                    version = getExtra("artifactVersion")
                    val outputFiles = file("$buildDir").listFiles()
                        ?.associate { it.name to it.takeIf { it.exists() } }.orEmpty()
                    val outputFile =
                        outputFiles["outputs"]?.resolve("aar/${project.name}-release.aar")
                            ?: outputFiles["libs"]?.resolve(
                                "${project.name}-${getExtra("artifactVersion")}.jar"
                            )
                    outputFile?.let { artifact(it) }
                    artifact(tasks["sourcesJar"])

                    pom.withXml {
                        val depsNode = asNode().appendNode("dependencies")
                        val allDeps = configurations.getByName("api").allDependencies +
                                configurations.getByName("implementation").allDependencies +
                                runCatching {
                                    configurations.getByName("pomOnly")
                                }.getOrNull()?.allDependencies.orEmpty()
                        allDeps.filter { it !is ProjectDependency }.forEach {
                            val depNode = depsNode.appendNode("dependency")
                            depNode.appendNode("groupId", it.group)
                            depNode.appendNode("artifactId", it.name)
                            depNode.appendNode("version", it.version)
                        }
                    }
                }
            }

            repositories {
                maven {
                    name = "GitHubPackages"
                    url = URI("https://maven.pkg.github.com/${System.getenv("GITHUB_REPOSITORY")}")
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN") // Github PAT
                    }
                }
                mavenLocal()
            }
        }
    }

}

task<Delete>("clean") {
    delete(fileTree(rootProject.buildDir))
}