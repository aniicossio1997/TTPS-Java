package com.grupo20.ttpsspringboot.dtos.bases;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaginateBaseDTO <T>  implements Serializable {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private List<T> elements;
}
