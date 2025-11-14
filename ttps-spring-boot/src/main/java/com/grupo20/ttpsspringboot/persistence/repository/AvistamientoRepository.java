package com.grupo20.ttpsspringboot.persistence.repository;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    /**
     * Equivalente a getByFilters(AvistamientoFilterDTO filter)
     * de tu AvistamientoDAOHibernateJPA.
     *
     * Si `usuarioId` es null → no filtra por usuario.
     * Si `publicacionId` es null → no filtra por publicación.
     * Si `publicacionId` NO es null → además exige que la publicación no esté borrada (deletedAt IS NULL).
     */
    @Query("""
        SELECT a
        FROM Avistamiento a
        WHERE
          (:#{#filter.usuarioId} IS NULL
             OR a.usuario.id = :#{#filter.usuarioId})
        AND (:#{#filter.publicacionId} IS NULL
             OR (a.publicacion.id = :#{#filter.publicacionId}
                 AND a.publicacion.deletedAt IS NULL))
        """)
    List<Avistamiento> getByFilters(@Param("filter") AvistamientoFilterDTO filter);

    // Opcionales: si algún día querés filtros simples:
    List<Avistamiento> findByUsuario_Id(Long usuarioId);

    List<Avistamiento> findByPublicacion_IdAndPublicacion_DeletedAtIsNull(Long publicacionId);
}
