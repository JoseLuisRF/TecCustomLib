package com.jlrf.tec.models

public data class Employee(
    val id: Long,
    val name: String,
    val salary: Double,
    val age: Int,
    val profileImage: String,
    val message: String? = null
)