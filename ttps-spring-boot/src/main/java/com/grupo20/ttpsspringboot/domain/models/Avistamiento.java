package com.grupo20.ttpsspringboot.domain.models;

import com.grupo20.ttpsspringboot.domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Getter
@Setter
public class Avistamiento extends IdentifiableEntity {

    @Column(length = 500) private String descripcion;
    @Column(nullable = false) private Date fecha;

    // Relaciones


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "avistamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

    @Column(nullable = false)
    private boolean agradecimiento = false;

    //--FIN RELACIONES

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }

    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }

    public void addFoto(Foto foto) {
        if (foto != null && !fotos.contains(foto)) {
            fotos.add(foto);
        }
    }

    @Override
    public String toString() {
        return "Avistamiento{" +
                "id=" + getId() +
                "descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", ubicacion=" + ubicacion +
                ", publicacion=" + publicacion +
                '}';
    }

    // --- Método add ---
    
    
}
