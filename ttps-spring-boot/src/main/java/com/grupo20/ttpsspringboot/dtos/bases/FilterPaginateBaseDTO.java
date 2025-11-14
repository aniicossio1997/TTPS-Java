package com.grupo20.ttpsspringboot.dtos.bases;

import lombok.Data;

import java.io.Serializable;

@Data
public class FilterPaginateBaseDTO implements Serializable {
    // 🔹 Paginación
    // número de página (0-based)
    private Integer page = 0;

    // cuántos registros por página
    private Integer size = 10;

    // 🔹 Orden (opcionales)
    private String sortBy = "fecha";     // nombre del campo en Publicacion
    private String sortDir = "DESC";     // "ASC" o "DESC"
}
