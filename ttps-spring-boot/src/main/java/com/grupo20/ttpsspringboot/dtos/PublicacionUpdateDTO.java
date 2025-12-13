package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionUpdateDTO {

    private String nombre;
    private String descripcion;
    private String color;
    private String especie;
    private String tamanio;

    private List<Long> agradecimientos;

    private UbicacionUpdateDTO ubicacion;

    private EstadoPublicacionEnum estado;
}
