package com.grupo20.ttpsspringboot.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UbicacionUpdateDTO implements Serializable {

    private String idExterno;

    private String provincia;

    private String ciudad;

    private String barrio;

    private Double latitud;

    private Double longitud;

}