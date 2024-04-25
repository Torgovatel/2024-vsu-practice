package ru.management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import ru.management.entity.Employee;
import ru.management.entity.Department;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDepartmentDTO {
    private Long id;
    @NotNull
    private Employee employee;
    @NotNull
    private Department department;
}
