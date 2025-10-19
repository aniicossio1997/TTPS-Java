package domain;

import domain.enums.MedallaEnum;
import java.util.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "Medalla")
public class Medalla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private MedallaEnum tipo;
    private Date fechaAsignacion;
    private Date fechaVencimiento;


    // --- getters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



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
