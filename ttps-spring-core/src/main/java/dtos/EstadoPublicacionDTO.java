package dtos;


import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class EstadoPublicacionDTO implements Serializable {
    private EstadoPublicacionEnum estado;
    private Date fecha;

    public static EstadoPublicacionDTO fromEntity(EstadoPublicacion estado) {
        EstadoPublicacionDTO dto = new EstadoPublicacionDTO();
        dto.setEstado(estado.getEstado());
        dto.setFecha(estado.getFecha());
        return dto;
    }
}