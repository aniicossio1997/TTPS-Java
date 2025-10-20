package domain.models;

import domain.enums.EstadoUsuarioEnum;
import domain.enums.RolUsuarioEnum;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

   // @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private Integer puntos;
    private Integer mascotasEnTransito;
    private Integer ayudadosEnZona;
    private EstadoUsuarioEnum estadosUsuario;


    private RolUsuarioEnum rol;

    //private Ubicacion ubicacion;
    //private Foto fotoPerfil;

    private List<Medalla> medallas = new ArrayList<>();
    private List<Publicacion> publicaciones = new ArrayList<>();
    private List<Avistamiento> avistamientosReportados = new ArrayList<>();

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




    public List<Medalla> getMedallas() {
        return medallas;
    }



    public List<Publicacion> getPublicaciones() {
        return publicaciones;
    }

    public List<Avistamiento> getAvistamientosReportados() {
        return avistamientosReportados;
    }





}