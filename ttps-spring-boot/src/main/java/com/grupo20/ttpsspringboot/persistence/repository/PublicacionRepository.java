package com.grupo20.ttpsspringboot.persistence.repository;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    // --- MÉTODOS SIMPLES (Estos quedan igual) ---

    @Query("SELECT p FROM Publicacion p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Publicacion> findByIdAndDeletedAtIsNull(@Param("id") Long id);

    List<Publicacion> findByUsuarioIdAndDeletedAtIsNull(Long usuarioId);

    @Query("SELECT p FROM Publicacion p WHERE p.deletedAt IS NULL AND p.id = :id")
    Optional<Publicacion> findActiveById(@Param("id") Long id);

    // ...otros métodos simples...

    // --- MÉTODO COMPLEJO (Todo en @Query) ---

    /**
     * Filtro dinámico que usa SpEL (:#{#...}) para leer el DTO.
     * La paginación y el orden vienen en el objeto 'pageable'.
     */
    @Query(value = """
    SELECT p FROM Publicacion p
    WHERE
        p.deletedAt IS NULL
        AND (:#{#filter.nombre} IS NULL OR :#{#filter.nombre} = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :#{#filter.nombre}, '%')))
        AND (:#{#filter.especie} IS NULL OR :#{#filter.especie} = '' OR p.especie = :#{#filter.especie})
        AND (:#{#filter.raza} IS NULL OR :#{#filter.raza} = '' OR p.raza = :#{#filter.raza})
        AND (:#{#filter.tamanio} IS NULL OR :#{#filter.tamanio} = '' OR p.tamanio = :#{#filter.tamanio})
        AND (:#{#filter.color} IS NULL OR :#{#filter.color} = '' OR p.color = :#{#filter.color})
        AND (:#{#filter.usuarioId} IS NULL OR p.usuario.id = :#{#filter.usuarioId})
        AND (:#{#filter.fechaDesde} IS NULL OR :#{#filter.fechaHasta} IS NULL OR p.fecha BETWEEN :#{#filter.fechaDesde} AND :#{#filter.fechaHasta})
        AND (:#{#filter.departamento} IS NULL OR :#{#filter.departamento} = '' OR LOWER(p.ubicacion.departamento) LIKE LOWER(CONCAT('%', :#{#filter.departamento}, '%')))
        AND (:#{#filter.provincia} IS NULL OR :#{#filter.provincia} = '' OR LOWER(p.ubicacion.provincia) LIKE LOWER(CONCAT('%', :#{#filter.provincia}, '%')))
       
        AND (:#{#filter.idExternoDepartamento} IS NULL OR :#{#filter.idExternoDepartamento} = '' OR p.ubicacion.idExternoDepartamento = :#{#filter.idExternoDepartamento})           
           
        AND (
              ( :#{#filter.includeEstados == null || #filter.includeEstados.isEmpty()} = true )
                 OR EXISTS (
                   SELECT 1
                   FROM EstadoPublicacion ep
                   WHERE ep.publicacion = p
                     AND ep.fecha = (
                       SELECT MAX(ep2.fecha)
                       FROM EstadoPublicacion ep2
                       WHERE ep2.publicacion = p
                     )
                     AND ep.estado IN :#{#filter.includeEstados}
                 )
               )
        AND (
              ( :#{#filter.excluidosEstados == null ? true : #filter.excluidosEstados.isEmpty()} = true )
        
                     OR
                     NOT EXISTS (
                       SELECT 1
                       FROM EstadoPublicacion ep
                       WHERE ep.publicacion = p
                         AND ep.fecha = (
                           SELECT MAX(ep2.fecha)
                           FROM EstadoPublicacion ep2
                           WHERE ep2.publicacion = p
                         )
                         AND ep.estado IN :#{#filter.excluidosEstados}
                     )
                )
                                                                                                                                
    """,
            countQuery = """
    SELECT COUNT(p) FROM Publicacion p
    WHERE
        p.deletedAt IS NULL
        AND (:#{#filter.nombre} IS NULL OR :#{#filter.nombre} = '' OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :#{#filter.nombre}, '%')))
        AND (:#{#filter.especie} IS NULL OR :#{#filter.especie} = '' OR p.especie = :#{#filter.especie})
        AND (:#{#filter.raza} IS NULL OR :#{#filter.raza} = '' OR p.raza = :#{#filter.raza})
        AND (:#{#filter.tamanio} IS NULL OR :#{#filter.tamanio} = '' OR p.tamanio = :#{#filter.tamanio})
        AND (:#{#filter.color} IS NULL OR :#{#filter.color} = '' OR p.color = :#{#filter.color})
        AND (:#{#filter.usuarioId} IS NULL OR p.usuario.id = :#{#filter.usuarioId})
        AND (:#{#filter.fechaDesde} IS NULL OR :#{#filter.fechaHasta} IS NULL OR p.fecha BETWEEN :#{#filter.fechaDesde} AND :#{#filter.fechaHasta})
        AND (:#{#filter.departamento} IS NULL OR :#{#filter.departamento} = '' OR LOWER(p.ubicacion.departamento) LIKE LOWER(CONCAT('%', :#{#filter.departamento}, '%')))
        AND (:#{#filter.provincia} IS NULL OR :#{#filter.provincia} = '' OR LOWER(p.ubicacion.provincia) LIKE LOWER(CONCAT('%', :#{#filter.provincia}, '%')))
        
        AND (:#{#filter.idExternoDepartamento} IS NULL OR :#{#filter.idExternoDepartamento} = '' OR p.ubicacion.idExternoDepartamento = :#{#filter.idExternoDepartamento})           
        
        AND (
         ( :#{#filter.includeEstados == null || #filter.includeEstados.isEmpty()} = true )
          OR EXISTS (
            SELECT 1
            FROM EstadoPublicacion ep
            WHERE ep.publicacion = p
              AND ep.fecha = (
                SELECT MAX(ep2.fecha)
                FROM EstadoPublicacion ep2
                WHERE ep2.publicacion = p
              )
              AND ep.estado IN :#{#filter.includeEstados}
          )
        ) 
       
          AND (
                ( :#{#filter.excluidosEstados == null ? true : #filter.excluidosEstados.isEmpty()} = true )
        
                     OR
                     NOT EXISTS (
                       SELECT 1
                       FROM EstadoPublicacion ep
                       WHERE ep.publicacion = p
                         AND ep.fecha = (
                           SELECT MAX(ep2.fecha)
                           FROM EstadoPublicacion ep2
                           WHERE ep2.publicacion = p
                         )
                         AND ep.estado IN :#{#filter.excluidosEstados}
                     )
                )
               
                                                                                                                                     
    """)
    Page<Publicacion> findByCaracteristicas(
            @Param("filter") PublicacionFilterDTO filter,
            Pageable pageable
    );
}