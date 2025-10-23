package domain.models;

import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/** FOTO: puede usarse en publicaciones y/o avistamientos */
@Entity
@Getter
@Setter
public class Foto extends IdentifiableEntity {


    @Column(length = 200, nullable = false)
    private String nombre;

    @Lob
    @Column(nullable = false)
    private byte[] content;
    // --- getters/setters ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getContent() { return content; }
    public void setContent(byte[] content) { this.content = content; }

    //RELACIONES
    // Usuario (foto de perfil: 0..1) → elegimos OneToOne
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    // Avistamiento (0..N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avistamiento_id", nullable = true)
    private Avistamiento avistamiento;

    // Publicación (1..N)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = true)
    private Publicacion publicacion;


    @Override
    public String toString() {
        return "Foto{"
                +"id=" + getId() +
                "nombre='" + nombre + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}