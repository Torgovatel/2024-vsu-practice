package ru.management.util.converters;

import org.springframework.stereotype.Component;

@Component
public interface EDTConverter<EntityType, DTOType> {
    DTOType toDTO(EntityType entity);

    EntityType toEntity(DTOType dto);
}