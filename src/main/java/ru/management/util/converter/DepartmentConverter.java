package ru.management.util.converter;

import ru.management.dto.DepartmentDTO;
import ru.management.entity.Department;

public class DepartmentConverter implements EDTConverter<Department, DepartmentDTO> {
    @Override
    public DepartmentDTO toDTO(Department entity) {
        return DepartmentDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .city(entity.getCity())
                .street(entity.getStreet())
                .region(entity.getRegion())
                .postalCode(entity.getPostalCode())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }
    @Override
    public Department toEntity(DepartmentDTO dto) {
        return Department.builder()
                .id(dto.getId())
                .name(dto.getName())
                .city(dto.getCity())
                .street(dto.getStreet())
                .region(dto.getRegion())
                .postalCode(dto.getPostalCode())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
