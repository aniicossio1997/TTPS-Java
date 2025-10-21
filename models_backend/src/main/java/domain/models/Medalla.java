package domain.models;

import domain.enums.MedallaEnum;

import java.util.Date;

import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;


@Entity
public class Medalla extends IdentifiableEntity {


    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private MedallaEnum tipo;

    @Column(nullable = false) private Date fechaAsignacion;
    private Date fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // --- getters ---

    public MedallaEnum getTipo() {
        return tipo;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }
}
