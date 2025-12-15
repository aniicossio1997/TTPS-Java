package com.grupo20.ttpsspringboot.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class AvistamientoUpdateDTO {

    private String descripcion;

    private UbicacionUpdateDTO ubicacion;

    private Boolean agradecimiento;

    private Date fecha;
}