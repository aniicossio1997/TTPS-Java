package com.grupo20.ttpsspringboot.dtos.mappingService;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioCreateMapperService {

    // De entidad a DTO chico
    UsuarioSmallDTO toSmallDto(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "puntos", constant = "0")
    @Mapping(target = "ayudadosEnZona", constant = "0")
    @Mapping(
            target = "estado",
            expression = "java(com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum.HABILITADO)"
    )
    @Mapping(target = "fotoPerfil", ignore = true)
    @Mapping(target = "publicaciones", ignore = true)
    @Mapping(target = "avistamientos", ignore = true)
    @Mapping(target = "medallas", ignore = true)
    Usuario toEntity(UsuarioCreateDTO dto);
}
