package com.grupo20.ttpsspringboot.dtos.mappingService;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UbicacionUpdateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)              // nunca tocamos el id
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "avistamientos", ignore = true)
    @Mapping(target = "publicaciones", ignore = true)
    void updateFromDto(UbicacionUpdateDTO dto, @MappingTarget Ubicacion entity);
}
