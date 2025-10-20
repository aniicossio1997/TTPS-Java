package domain.models;

import domain.enums.EstadoPublicacionEnum;

import java.time.LocalDate;

public class EstadoPublicacion {
    private Integer id;
    private LocalDate fecha;
    private EstadoPublicacionEnum estado;

    // --- getters/setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public EstadoPublicacionEnum getEstado() { return estado; }
    public void setEstado(EstadoPublicacionEnum estado) { this.estado = estado; }
}
