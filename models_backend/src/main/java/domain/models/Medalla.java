package domain.models;

import domain.enums.MedallaEnum;
import java.util.Date;

import jakarta.persistence.*;



public class Medalla {


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
