package ru.management.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.management.store.entities.Department;
import ru.management.store.entities.Employee;

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
