package ru.management.util.converter;
public interface EDTConverter<EntityType, DTOType> {
    DTOType toDTO(EntityType entity);
    EntityType toEntity(DTOType dto);
}