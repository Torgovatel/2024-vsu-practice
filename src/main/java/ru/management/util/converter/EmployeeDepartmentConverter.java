package ru.management.util.converter;

import org.springframework.stereotype.Component;
import ru.management.dto.EmployeeDepartmentDTO;
import ru.management.entity.EmployeeDepartment;

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
