package ru.management.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.management.store.entities.Department;
import ru.management.store.entities.Employee;

@Data
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
