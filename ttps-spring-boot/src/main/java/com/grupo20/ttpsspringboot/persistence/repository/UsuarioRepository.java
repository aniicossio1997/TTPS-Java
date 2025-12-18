package com.grupo20.ttpsspringboot.persistence.repository;

import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository // Indica que es un componente de Spring para acceso a datos
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /** * Implementación del login y búsquedas. Spring Data infiere la consulta:
     * SELECT u FROM Usuario u WHERE u.email = ?1
     */
    Optional<Usuario> findByEmail(String email);

    // --- Métodos necesarios para el Borrado Lógico y la funcionalidad Admin ---

    /** * Para que los usuarios normales solo puedan ver HABILITADOS.
     * Consulta inferida: SELECT u FROM Usuario u WHERE u.estado = ?1
     */
    List<Usuario> findAllByEstado(EstadoUsuarioEnum estado);

    /**
     * Para que un usuario normal solo vea un ID si está HABILITADO.
     * Consulta inferida: SELECT u FROM Usuario u WHERE u.id = ?1 AND u.estado = ?2
     */
    Optional<Usuario> findByIdAndEstado(Long id, EstadoUsuarioEnum estado);



    @Query("""
        SELECT u 
        FROM Usuario u 
        WHERE u.puntos > 10
        ORDER BY u.puntos DESC
        LIMIT 10
    """)
    List<Usuario> ranking();

    List<Usuario> findTop100ByPuntosGreaterThanOrderByPuntosDesc(int puntos);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);


    long countByRolAndEstado(
            RolUsuarioEnum rol,
            EstadoUsuarioEnum estado
    );

    // UsuarioRepository.java
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.ubicacion WHERE u.id = :id")
    Optional<Usuario> findByIdWithUbicacion(@Param("id") Long id);
}