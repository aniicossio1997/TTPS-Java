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

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;


    private String provincia;
    private String idExternoProvincia;

    private String municipio;
    private String idExternoMunicipio;

    private String departamento;
    private String idExternoDepartamento;


    // --- Relaciones inversas (1 Ubicacion : N Publicaciones/Avistamientos/Usuarios)

    @OneToMany(mappedBy = "ubicacion") private List<Usuario> usuarios = new ArrayList<>();
    @OneToMany(mappedBy = "ubicacion") private List<Avistamiento> avistamientos = new ArrayList<>();
    @OneToMany(mappedBy = "ubicacion") private List<Publicacion> publicaciones = new ArrayList<>();



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ubicacion ubicacion = (Ubicacion) o;
        return Objects.equals(latitud, ubicacion.latitud) &&
                Objects.equals(longitud, ubicacion.longitud) && Objects.equals(provincia, ubicacion.provincia) &&
                Objects.equals(idExternoProvincia, ubicacion.idExternoProvincia) && Objects.equals(municipio, ubicacion.municipio) && Objects.equals(idExternoMunicipio, ubicacion.idExternoMunicipio) && Objects.equals(departamento, ubicacion.departamento) && Objects.equals(idExternoDepartamento, ubicacion.idExternoDepartamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), latitud, longitud, provincia, idExternoProvincia, municipio, idExternoMunicipio, departamento, idExternoDepartamento);
    }

    public String hola(){
        return "UBICACION";
    }
}