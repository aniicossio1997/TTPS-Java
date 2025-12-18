package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UbicacionUpdateDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;

    // 2. Lógica de negocio (validación)
    public void _validar(Ubicacion u) {
        if (u == null) {
            throw new IllegalArgumentException("La ubicación no puede ser null.");
        }
        if (u.getLatitud() == null) {
            throw new IllegalArgumentException("Latitud obligatoria (no puede ser null).");
        }
        if (u.getLongitud() == null) {
            throw new IllegalArgumentException("Longitud obligatoria (no puede ser null).");
        }
    }

    // --- Métodos CRUD básicos ---
    @Transactional
    public Ubicacion crearUbicacion(UbicacionCreateDTO dto) {
        Ubicacion ubicacion = dto.toEntity();
        _validar(ubicacion); // Aplicamos la lógica de negocio
        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public Ubicacion updateUbicacion(Long id, UbicacionUpdateDTO dto) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));
        if (ubicacion == null) {
            throw new NotFoundException();
        }

        if (dto.getIdExternoProvincia() != null) ubicacion.setIdExternoProvincia(dto.getIdExternoProvincia());
        if (dto.getProvincia() != null) ubicacion.setProvincia(dto.getProvincia());

        if (dto.getIdExternoMunicipio() != null) ubicacion.setIdExternoMunicipio(dto.getIdExternoMunicipio());
        if (dto.getMunicipio() != null) ubicacion.setMunicipio(dto.getMunicipio());

        if (dto.getIdExternoDepartamento() != null) ubicacion.setIdExternoDepartamento(dto.getIdExternoDepartamento());
        if (dto.getDepartamento() != null) ubicacion.setDepartamento(dto.getDepartamento());


        if (dto.getLatitud() != null) ubicacion.setLatitud(dto.getLatitud());
        if (dto.getLongitud() != null) ubicacion.setLongitud(dto.getLongitud());

        return ubicacionRepository.save(ubicacion);
    }



    /**
     * Busca una ubicación por su ID.
     * @Transactional(readOnly = true) es una optimización para consultas.
     * @param id El ID de la ubicación.
     * @return La entidad Ubicacion o null si no se encuentra.
     */
    @Transactional(readOnly = true)
    public Ubicacion getUbicacion(Long id) {
        return this.ubicacionRepository.findById(id).orElse(null);
    }

    /**
     * Devuelve todas las ubicaciones.
     * @return Una lista de todas las entidades Ubicacion.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionRepository.findAll();
    }

    /**
     * Busca ubicaciones por provincia.
     * @param provincia El nombre de la provincia.
     * @return Lista de ubicaciones que coinciden.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> buscarPorProvincia(String provincia) {
        return ubicacionRepository.findByProvincia(provincia);
    }

    /**
     * Búsqueda flexible por múltiples criterios (LIKE).
     * @param idExternoProvincia
     * @param provincia
     * @param idExternoMunicipio
     * @param municipio
     * @param idExternoDepartamento
     * @param departamento
     * @return Lista de ubicaciones que coinciden.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> buscarPorCriterio(String idExternoProvincia, String provincia, String idExternoMunicipio, String municipio, String idExternoDepartamento,String departamento) {
        return this.ubicacionRepository.findByCriteriaLike(idExternoProvincia, provincia, idExternoMunicipio, municipio, idExternoDepartamento, departamento);
    }
}