package com.grupo20.ttpsspringboot.dtos;


import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import com.grupo20.ttpsspringboot.domain.models.EstadoPublicacion;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import jakarta.validation.constraints.NotBlank; // Paquete 'jakarta'
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionCreateDTO {

    @NotBlank
    private String nombre;

    private String descripcion; // Opcional

    @NotBlank
    private String color;

    @NotBlank
    private String especie;

    @NotBlank
    private String tamanio;

    @NotNull
    private UbicacionCreateDTO ubicacion;

    @NotNull(message = "El estado inicial es obligatorio")
    private EstadoPublicacionEnum estado;

    public Publicacion toEntity() {
        Publicacion entity = new Publicacion();
        entity.setNombre(this.nombre);
        entity.setDescripcion(this.descripcion);
        entity.setColor(this.color);
        entity.setEspecie(this.especie);
        entity.setTamanio(this.tamanio);
        entity.addEstado(new EstadoPublicacion(estado, entity));
        return entity;
    }
}