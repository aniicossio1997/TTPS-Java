package com.grupo20.ttpsspringboot.dtos.bases;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UbicacionBaseDTO  implements Serializable {

    @NotNull(message = "La latitud es obligatoria.")
    private Double latitud;
    @NotNull(message = "La longitud es obligatoria.")
    private Double longitud;


    private String provincia;
    private String idExternoProvincia;

    private String municipio;
    private String idExternoMunicipio;

    private String departamento;
    private String idExternoDepartamento;
}
