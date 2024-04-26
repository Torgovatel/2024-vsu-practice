package ru.management.util.converter;

import ru.management.dto.EmployeeDTO;
import ru.management.entity.Employee;

public class EmployeeConverter implements EDTConverter<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO toDTO(Employee entity) {
        return EmployeeDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .passportNumber(entity.getPassportNumber())
                .passportDate(entity.getPassportDate())
                .salary(entity.getSalary())
                .build();
    }

    @Override
    public Employee toEntity(EmployeeDTO dto) {
        return Employee.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .passportNumber(dto.getPassportNumber())
                .passportDate(dto.getPassportDate())
                .salary(dto.getSalary())
                .build();
    }
}
