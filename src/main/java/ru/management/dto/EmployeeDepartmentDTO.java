package ru.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.management.entity.Employee;
import ru.management.entity.Department;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDepartmentDTO {
    private Long id;
    private Employee employee;
    private Department department;
}
