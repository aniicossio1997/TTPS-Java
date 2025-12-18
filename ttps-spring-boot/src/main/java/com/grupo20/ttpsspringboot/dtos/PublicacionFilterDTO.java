package com.grupo20.ttpsspringboot.dtos;


import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import com.grupo20.ttpsspringboot.dtos.bases.FilterPaginateBaseDTO;
import io.swagger.v3.oas.annotations.Parameter; // Importante
import io.swagger.v3.oas.annotations.enums.Explode; // Importante
import io.swagger.v3.oas.annotations.enums.ParameterStyle; // Importante
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    // --- AQUÍ ESTÁ EL CAMBIO ---
    @Parameter(
            description = "Estados de la publicación (separados por coma)",
            style = ParameterStyle.FORM,
            explode = Explode.FALSE  // FALSE = separado por comas (A,B). TRUE = repetido (param=A&param=B)
    )
    public List<EstadoPublicacionEnum> includeEstados;

    @Parameter(
            description = "Estados de la publicación (separados por coma)",
            style = ParameterStyle.FORM,
            explode = Explode.FALSE  // FALSE = separado por comas (A,B). TRUE = repetido (param=A&param=B)
    )
    public List<EstadoPublicacionEnum> excluidosEstados;

    // Ubicacion
    public String departamento;
    public String provincia;
    public String idExternoDepartamento;
}
