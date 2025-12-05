package com.grupo20.ttpsspringboot.dtos.mappingService;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UbicacionMapperService {
    // Crear NUEVA ubicacion (id queda null, lo genera JPA)
    @Mapping(target = "id", ignore = true) // viene de IdentifiableEntity
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "avistamientos", ignore = true)
    @Mapping(target = "publicaciones", ignore = true)
    Ubicacion toEntity(UbicacionUpdateDTO dto);

    // Actualizar una Ubicacion EXISTENTE sin tocar el id
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "avistamientos", ignore = true)
    @Mapping(target = "publicaciones", ignore = true)
    void updateEntityFromDto(UbicacionUpdateDTO dto, @MappingTarget Ubicacion entity);
}
