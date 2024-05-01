package ru.management.util.converters;

import org.springframework.stereotype.Component;
import ru.management.api.dto.EmployeeDepartmentDTO;
import ru.management.store.entities.EmployeeDepartment;

@Component
public class EmployeeDepartmentConverter implements EDTConverter<EmployeeDepartment, EmployeeDepartmentDTO> {
    @Override
    public EmployeeDepartmentDTO toDTO(EmployeeDepartment entity) {
        return EmployeeDepartmentDTO.builder()
                .id(entity.getId())
                .employee(entity.getEmployee())
                .department(entity.getDepartment())
                .build();
    }

    @Override
    public EmployeeDepartment toEntity(EmployeeDepartmentDTO dto) {
        return EmployeeDepartment.builder()
                .id(dto.getId())
                .employee(dto.getEmployee())
                .department(dto.getDepartment())
                .build();
    }
}
