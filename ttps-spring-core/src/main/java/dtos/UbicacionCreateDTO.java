package dtos;

import domain.models.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UbicacionCreateDTO implements Serializable {

    private String idExterno;

    @NotBlank(message = "La provincia es obligatoria.")
    private String provincia;

    @NotBlank(message = "La ciudad es obligatoria.")
    private String ciudad;

    @NotBlank(message = "El barrio es obligatorio.")
    private String barrio;

    @NotNull(message = "La latitud es obligatoria.")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria.")
    private Double longitud;

    public Ubicacion toEntity() {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setIdExterno(this.idExterno);
        ubicacion.setProvincia(this.provincia);
        ubicacion.setCiudad(this.ciudad);
        ubicacion.setBarrio(this.barrio);
        ubicacion.setLatitud(this.latitud);
        ubicacion.setLongitud(this.longitud);
        return ubicacion;
    }
}