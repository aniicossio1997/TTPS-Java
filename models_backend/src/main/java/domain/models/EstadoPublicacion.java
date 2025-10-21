package domain.models;

import domain.enums.EstadoPublicacionEnum;
import domain.models.base.IdentifiableEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "estado_publicacion")
public class EstadoPublicacion extends IdentifiableEntity {
    @Column(nullable = false) private LocalDate fecha;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EstadoPublicacionEnum estado; // PERDIDO_PROPIO, PERDIDO_AJENO, RECUPERADO, ADOPTADO

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;
}
