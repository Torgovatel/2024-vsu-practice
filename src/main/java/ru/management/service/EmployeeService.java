package ru.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.management.dto.EmployeeDTO;
import ru.management.entity.Employee;
import ru.management.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Transactional
    public void createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = Employee.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .passportNumber(employeeDTO.getPassportNumber())
                .passportDate(employeeDTO.getPassportDate())
                .salary(employeeDTO.getSalary())
                .build();
        employeeRepository.save(employee);
    }
}