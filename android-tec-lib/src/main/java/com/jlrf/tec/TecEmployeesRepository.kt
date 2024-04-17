package com.jlrf.tec

import com.jlrf.tec.models.Employee

public interface TecEmployeesRepository {

    public fun getMyProfile() : Employee

    public fun getEmployeesReadOnly() : List<Employee>
}