package domain.models;

/** FOTO: puede usarse en publicaciones y/o avistamientos */
public class Foto {
    private Integer id;
    private String nombre;
    private byte[] content;

    // --- getters/setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }
}