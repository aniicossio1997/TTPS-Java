package com.grupo20.ttpsspringboot.persistence.repository;


import com.grupo20.ttpsspringboot.domain.models.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    // Opcionales, por si querés filtrar
    List<Foto> findByPublicacion_Id(Long publicacionId);

    List<Foto> findByAvistamiento_Id(Long avistamientoId);

    // 0..1 foto de perfil
    Optional<Foto> findByUsuario_Id(Long usuarioId);
}

