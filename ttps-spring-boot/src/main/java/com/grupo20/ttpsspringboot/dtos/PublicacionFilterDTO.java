package com.grupo20.ttpsspringboot.dtos;


import com.grupo20.ttpsspringboot.dtos.bases.FilterPaginateBaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class PublicacionFilterDTO extends FilterPaginateBaseDTO {
    public String nombre;
    public String especie;
    public String raza;
    public String tamanio;
    public String color;
    public Date fechaDesde;
    public Date fechaHasta;
    public Long usuarioId;


}
