package com.grupo20.ttpsspringboot.domain.models;


import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Usuario extends IdentifiableEntity {

    private String nombre;
    private String apellido;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private Integer puntos;
    private Integer ayudadosEnZona;


    //CONFIG DE BASE
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EstadoUsuarioEnum estado = EstadoUsuarioEnum.HABILITADO;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private RolUsuarioEnum rol;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;

    // 0..1 foto de perfil
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Foto fotoPerfil;

    // relaciones de actividad
    @OneToMany(mappedBy = "usuario") private List<Publicacion> publicaciones = new ArrayList<>();
    @OneToMany(mappedBy = "usuario") private List<Avistamiento> avistamientos = new ArrayList<>();

    // medallas del usuario
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medalla> medallas = new ArrayList<>();

    //FIN DE RELACIONES
    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Usuario(String nombre, String apellido, String email, String password, Integer puntos, Integer mascotasEnTransito, Integer ayudadosEnZona, RolUsuarioEnum rol, Ubicacion ubicacion, Foto fotoPerfil) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.puntos = puntos;
        this.ayudadosEnZona = ayudadosEnZona;
        this.rol = rol;
        this.ubicacion = ubicacion;
        this.fotoPerfil = fotoPerfil;
    }

    public Integer getAyudadosEnZona() {
        return ayudadosEnZona;
    }
    public void setAyudadosEnZona(int ayudadosEnZona) {
        this.ayudadosEnZona = ayudadosEnZona;
    }

    public RolUsuarioEnum getRol() {
        return rol;
    }
    public void setRol(RolUsuarioEnum rol) { this.rol = rol; }








}