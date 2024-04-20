package com.jlrf.tec

import com.jlrf.tec.models.Employee

public class TecEmployeeRepositoryImpl : TecEmployeesRepository {

    override fun getMyProfile(): Employee {
        return Employee(
            id = 1234567120L,
            name = "Jose Ramon",
            salary = 0.0,
            age = 22,
            profileImage = "",
            message = "Oh si hermano"
        )
    }

    override fun getEmployeesReadOnly(): List<Employee> {
        return listOf(
            Employee(
                id = 1234567890L,
                name = "Jose Luis",
                salary = 23000.0,
                age = 30,
                profileImage = ""
            ),
            Employee(
                id = 1234567891L,
                name = "John Petrucci",
                salary = 35000.0,
                age = 45,
                profileImage = ""
            ),
            Employee(
                id = 1234567892L,
                name = "Mike Portnoy",
                salary = 45000.0,
                age = 45,
                profileImage = ""
            ),
            Employee(
                id = 1234567893L,
                name = "Jose Ramon",
                salary = 999999.99999,
                age = 22,
                profileImage = "",
                message = "Hola Linux"
            )
        )
    }
}
