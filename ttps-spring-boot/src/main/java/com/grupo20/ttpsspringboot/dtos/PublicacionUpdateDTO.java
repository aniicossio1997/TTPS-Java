package com.grupo20.ttpsspringboot.dtos;

import domain.enums.EstadoPublicacionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionUpdateDTO {

    private String nombre;
    private String descripcion;
    private String color;
    private String especie;
    private String raza;
    private String tamanio;

    private UbicacionCreateDTO ubicacion;

    private EstadoPublicacionEnum estado;
}
