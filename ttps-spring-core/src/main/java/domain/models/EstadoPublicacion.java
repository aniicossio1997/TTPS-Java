package domain.models;

import domain.enums.EstadoPublicacionEnum;
import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "estado_publicacion")
public class EstadoPublicacion extends IdentifiableEntity {

    @Getter
    @Column(nullable = false) private Date fecha;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EstadoPublicacionEnum estado; // PERDIDO_PROPIO, PERDIDO_AJENO, RECUPERADO, ADOPTADO

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @Override
    public String toString() {
        return "EstadoPublicacion{" +
                "id=" + getId() +
                "estado=" + estado +
                ", fecha=" + fecha +
                '}';
    }

    public EstadoPublicacion(){}
    public EstadoPublicacion(Date fecha, EstadoPublicacionEnum estado, Publicacion publicacion) {
        this.fecha = fecha;
        this.estado = estado;
        this.publicacion = publicacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public EstadoPublicacionEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacionEnum estado) {
        this.estado = estado;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }
}
