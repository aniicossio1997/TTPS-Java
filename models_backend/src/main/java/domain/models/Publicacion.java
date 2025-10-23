package domain.models;

import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@Entity
public class Publicacion extends IdentifiableEntity {

    private String nombre;
    private String descripcion;
    private Date fecha;
    private String color;
    private String especie;
    private String raza;
    private String tamanio;

    //-- Relaciones
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // creador/dueño

    // 1..N por regla de negocio (validar en servicio o con Bean Validation)
    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Foto> fotos = new ArrayList<>();

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadoPublicacion> estados = new ArrayList<>();

    @OneToMany(mappedBy = "publicacion")
    private List<Avistamiento> avistamientos = new ArrayList<>();

    public EstadoPublicacion getEstado() {
        Optional<EstadoPublicacion> estadoReciente = this.estados.stream()
                .max(Comparator.comparing(EstadoPublicacion::getFecha));

        return estadoReciente.orElse(null);
    }
    public void addFoto(Foto foto) {
        if (foto != null && !fotos.contains(foto)) {
            fotos.add(foto);
        }
    }

    public void addAvistamiento(Avistamiento avistamiento) {
        if (avistamiento != null && !avistamientos.contains(avistamiento)) {
            avistamientos.add(avistamiento);
        }
    }

    public void addEstado(EstadoPublicacion estado) {
        if (estado != null) {
            estados.add(estado);
        }
    }
}