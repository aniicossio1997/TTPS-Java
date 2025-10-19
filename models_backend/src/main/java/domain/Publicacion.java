package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Publicacion {
    private Integer id;
    private String nombre;
    private String descripcion;
    private LocalDate fecha;
    private String color;
    private String especie;
    private String raza;
    private String tamanio;

    // Relaciones
    private List<Foto> fotos = new ArrayList<>();
    private List<Avistamiento> avistamientos = new ArrayList<>();
    private List<EstadoPublicacion> estados = new ArrayList<>();

    // --- getters ---

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getFecha() {
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