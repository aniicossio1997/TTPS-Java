package dtos;

import domain.models.Ubicacion;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO implements Serializable {

    private Long id;
    private String idExterno;
    private String provincia;
    private String ciudad;
    private String barrio;
    private Double latitud;
    private Double longitud;

    public static UbicacionDTO fromEntity(Ubicacion entity) {
        UbicacionDTO dto = new UbicacionDTO();

        dto.setId(entity.getId());
        dto.setIdExterno(entity.getIdExterno());
        dto.setProvincia(entity.getProvincia());
        dto.setCiudad(entity.getCiudad());
        dto.setBarrio(entity.getBarrio());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());

        return dto;
    }
}
