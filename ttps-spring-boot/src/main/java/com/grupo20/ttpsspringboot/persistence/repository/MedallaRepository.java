package com.grupo20.ttpsspringboot.persistence.repository;

import com.grupo20.ttpsspringboot.domain.models.Medalla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedallaRepository extends JpaRepository<Medalla, Long> {

    // Equivalente a getByUsuarioId(Long usuarioId)
    List<Medalla> findByUsuarioId(Long usuarioId);
    // (usa la relación m.usuario.id)
}
