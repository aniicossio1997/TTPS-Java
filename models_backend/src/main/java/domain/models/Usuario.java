package domain.models;

import domain.enums.EstadoUsuarioEnum;
import domain.enums.RolUsuarioEnum;
import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario extends IdentifiableEntity {

    private String nombre;
    private String apellido;

   // @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private Integer puntos;
    private Integer mascotasEnTransito;
    private Integer ayudadosEnZona;


    //CONFIG DE BASE
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EstadoUsuarioEnum estado;

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
        this.mascotasEnTransito = mascotasEnTransito;
        this.ayudadosEnZona = ayudadosEnZona;
        this.rol = rol;

       // this.ubicacion = ubicacion;
        // this.fotoPerfil = fotoPerfil;
    }

    public Usuario(String nombre, String apellido, String email, String password) {
    }

    public Usuario() {

    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getMascotasEnTransito() {
        return mascotasEnTransito;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public Integer getAyudadosEnZona() {
        return ayudadosEnZona;
    }

    public RolUsuarioEnum getRol() {
        return rol;
    }








}