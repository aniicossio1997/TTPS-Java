package com.grupo20.ttpsspringboot.dtos.mappingService;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UsuarioUpdateMapper {


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "puntos", ignore = true)          // si no querés que se actualicen acá
    @Mapping(target = "ayudadosEnZona", ignore = true)  // idem
    //@Mapping(target = "estado", ignore = true)          // si el estado se cambia en otra lógica
    @Mapping(target = "fotoPerfil", ignore = true)
    @Mapping(target = "publicaciones", ignore = true)
    @Mapping(target = "avistamientos", ignore = true)
    @Mapping(target = "medallas", ignore = true)
    @Mapping(target = "ubicacion", ignore = true)       // la tratamos aparte
    @Mapping(target = "estado", ignore = true)

    void updateFromDto(UsuarioUpdateDTO dto, @MappingTarget Usuario entity);
}

