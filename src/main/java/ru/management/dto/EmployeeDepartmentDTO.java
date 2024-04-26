package ru.management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.management.entity.Department;
import ru.management.entity.Employee;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDepartmentDTO {
    @Positive
    private Long id;
    @NotNull
    private Employee employee;
    @NotNull
    private Department department;
}
