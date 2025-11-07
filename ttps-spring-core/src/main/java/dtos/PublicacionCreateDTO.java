package dtos;

import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import domain.models.Publicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublicacionCreateDTO {

    @NotBlank
    private String nombre;

    private String descripcion; // Opcional

    @NotBlank
    private String color;

    @NotBlank
    private String especie;

    @NotBlank
    private String raza;

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
        entity.setRaza(this.raza);
        entity.setTamanio(this.tamanio);
        entity.addEstado(new EstadoPublicacion(estado, entity));
        return entity;
    }
}