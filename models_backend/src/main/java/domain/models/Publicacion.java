package domain.models;

import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //--FIN de RELACIONE

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
    // --- getters ---

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getColor() {
        return color;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public List<Avistamiento> getAvistamientos() {
        return avistamientos;
    }

    public List<EstadoPublicacion> getEstados() {
        return estados;
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