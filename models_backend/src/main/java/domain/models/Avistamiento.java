package domain.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Avistamiento {
    private Integer id;
    private String descripcion;
    private LocalDate fecha;

    // Relaciones
    private Publicacion publicacion;
    private Usuario usuario; // Usuario que realizó el avistamiento
    private List<Foto> fotos = new ArrayList<>();


    // --- getters/setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Publicacion getPublicacion() { return publicacion; }
    public void setPublicacion(Publicacion publicacion) { this.publicacion = publicacion; }

    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }

    // --- Método add ---
    public void addFoto(Foto foto) {
        fotos.add(foto);
    }
}
