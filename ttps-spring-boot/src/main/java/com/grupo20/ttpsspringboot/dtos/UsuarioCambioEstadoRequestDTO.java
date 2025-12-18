package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCambioEstadoRequestDTO {
    private EstadoUsuarioEnum estado;
}
