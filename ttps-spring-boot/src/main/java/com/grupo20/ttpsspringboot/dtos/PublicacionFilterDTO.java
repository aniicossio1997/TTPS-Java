package com.grupo20.ttpsspringboot.dtos;


import lombok.Data;

import java.util.Date;

@Data
public class PublicacionFilterDTO {
    public String nombre;
    public String especie;
    public String raza;
    public String tamanio;
    public String color;
    public Date fechaDesde;
    public Date fechaHasta;
    public int offset = 0;
    public int maxResults = 20;
}
