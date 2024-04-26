package ru.management.util.converter;

import org.springframework.stereotype.Component;

@Component
public interface EDTConverter<EntityType, DTOType> {
    DTOType toDTO(EntityType entity);

    EntityType toEntity(DTOType dto);
}