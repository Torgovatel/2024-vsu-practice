package ru.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.management.entity.Employee;
import ru.management.entity.EmployeeDepartment;
import ru.management.entity.Department;
import ru.management.repository.EmployeeRepository;
import ru.management.repository.DepartmentRepository;
import ru.management.repository.EmployeeDepartmentRepository;

@Service
public class ManagementService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeDepartmentRepository employeeDepartmentRepository;

    @Transactional
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional
    public void updateDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Transactional
    public void updateEmployeeDepartment(EmployeeDepartment employeeDepartment) {
        employeeDepartmentRepository.save(employeeDepartment);
    }
}
