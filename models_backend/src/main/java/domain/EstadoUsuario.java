package domain;

import domain.enums.EstadoUsuarioEnum;
import java.util.Date;

public class EstadoUsuario {
    private Integer id;
    private Date fecha;
    private EstadoUsuarioEnum estado;

    public Integer getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public EstadoUsuarioEnum getEstado() {
        return estado;
    }
}
