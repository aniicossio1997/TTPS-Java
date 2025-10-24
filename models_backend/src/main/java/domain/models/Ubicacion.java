package domain.models;

import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity // Indica que esta clase es una entidad persistente
public class Ubicacion  extends IdentifiableEntity {

    private String idExterno;

    private String provincia;

    private String ciudad;

    private String barrio;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;


    public Ubicacion(String idExterno, String provincia, String ciudad, String barrio, Double latitud, Double longitud) {
        this.idExterno = idExterno;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public  Ubicacion() {   }

    // --- Relaciones inversas (1 Ubicacion : N Publicaciones/Avistamientos/Usuarios)

    @OneToMany(mappedBy = "ubicacion") private List<Usuario> usuarios = new ArrayList<>();
    @OneToMany(mappedBy = "ubicacion") private List<Avistamiento> avistamientos = new ArrayList<>();
    @OneToMany(mappedBy = "ubicacion") private List<Publicacion> publicaciones = new ArrayList<>();

    @Override
    public String toString() {
        return "Ubicacion{" +
                "id=" + getId() + // Asumiendo que tienes un getId() o el campo 'id'
                ", provincia='" + provincia + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", barrio='" + barrio + '\'' +
                '}';
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ubicacion ubicacion)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(idExterno, ubicacion.idExterno) && Objects.equals(provincia, ubicacion.provincia) && Objects.equals(ciudad, ubicacion.ciudad) && Objects.equals(barrio, ubicacion.barrio) && Objects.equals(latitud, ubicacion.latitud) && Objects.equals(longitud, ubicacion.longitud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idExterno, provincia, ciudad, barrio, latitud, longitud);
    }
}