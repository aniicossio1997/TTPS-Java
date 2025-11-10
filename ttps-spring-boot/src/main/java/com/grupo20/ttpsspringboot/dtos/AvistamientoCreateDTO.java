package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvistamientoCreateDTO {

    @NotBlank
    private String descripcion;

    @NotNull
    private UbicacionCreateDTO ubicacion;

    @NotNull
    private Long publicacionId;

    public Avistamiento toEntity() {
        Avistamiento avistamiento = new Avistamiento();
        avistamiento.setDescripcion(this.descripcion);

        return avistamiento;
    }
}
