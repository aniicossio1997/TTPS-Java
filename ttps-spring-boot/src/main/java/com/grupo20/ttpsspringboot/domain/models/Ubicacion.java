package com.grupo20.ttpsspringboot.domain.models;


import com.grupo20.ttpsspringboot.domain.models.base.IdentifiableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity // Indica que esta clase es una entidad persistente
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component("Ubicacion")
public class Ubicacion  extends IdentifiableEntity {

    private String idExterno;

    private String provincia;

    private String ciudad;

    private String barrio;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;


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





    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idExterno, provincia, ciudad, barrio, latitud, longitud);
    }

    public String hola(){
        return "UBICACION";
    }
}