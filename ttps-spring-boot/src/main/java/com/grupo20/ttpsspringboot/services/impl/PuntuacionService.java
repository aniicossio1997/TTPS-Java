package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.constants.Puntuacion;
import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import com.grupo20.ttpsspringboot.domain.enums.MedallaEnum;
import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Medalla;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.persistence.repository.AvistamientoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.MedallaRepository;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import com.grupo20.ttpsspringboot.services.IPuntuacionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PuntuacionService implements IPuntuacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AvistamientoRepository avistamientoRepository;

    @Autowired
    private MedallaRepository medallaRepository;

    @Autowired
    private UbicacionService ubicacionService;

    private final int DURACION_NUEVO_TUTOR = 30; // 30 DIAS

    @Transactional
    public void otorgarPuntajePorAdopcion(Usuario usuario) {
        evaluarMedallasPorAdopcion(usuario);
        otorgarPuntos(usuario, Puntuacion.POR_ADOPCION_MASCOTA);
    }

    @Transactional
    public void otorgarPuntajesPorHallazgo(Publicacion publicacion, List<Long> agradecimientos) {
        if (publicacion.getEstadoEnum() == EstadoPublicacionEnum.RECUPERADO && agradecimientos != null && !agradecimientos.isEmpty()) {
            // Filtrar los avistamientos asociados a la publicación que fueron agradecidos
            List<Avistamiento> avistamientos = publicacion.getAvistamientos().stream()
                    .filter(a -> agradecimientos.contains(a.getId()))
                    .toList();

            for (Avistamiento avistamiento : avistamientos) {
                avistamiento.setAgradecimiento(true);
                avistamientoRepository.save(avistamiento);
            }

            // Obtener los usuarios únicos de esos avistamientos
            Set<Usuario> usuariosAgradecidos = avistamientos.stream()
                    .map(Avistamiento::getUsuario)
                    .collect(Collectors.toSet());

            // Dar puntaje a cada usuario agradecido
            for (Usuario u : usuariosAgradecidos) {
                otorgarPuntos(u, Puntuacion.POR_HALLAZGO_MASCOTA);
            }
        }
    }

    @Transactional
    public void otorgarPuntosPorReporte(Usuario usuario) {
       otorgarPuntos(usuario, Puntuacion.POR_REPORTE_MASCOTA);
    }

    private void otorgarPuntos(Usuario usuario, Integer puntos) {
        usuario.addPuntos(puntos);
        evaluarMedallasPorPuntaje(usuario);
        usuarioRepository.save(usuario);
    }

    private void evaluarMedallasPorAdopcion(Usuario usuario) {
        List<Medalla> medallas = medallaRepository.findByUsuarioId(usuario.getId());
        Optional<Medalla> medallaExistente = medallas.stream()
                .filter(m -> m.getTipo() == MedallaEnum.NUEVO_TUTOR)
                .findFirst();

        Date ahora = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(ahora);
        cal.add(Calendar.DAY_OF_MONTH, DURACION_NUEVO_TUTOR);
        Date vencimiento = cal.getTime();

        // Si ya la tiene le ajustamos la fecha, si no creamos una
        if (medallaExistente.isPresent()) {
            Medalla medalla = medallaExistente.get();
            medalla.setFechaAsignacion(ahora);
            medalla.setFechaVencimiento(vencimiento);
            medallaRepository.save(medalla);
        } else {
            Medalla nueva = new Medalla();
            nueva.setTipo(MedallaEnum.NUEVO_TUTOR);
            nueva.setFechaAsignacion(ahora);
            nueva.setFechaVencimiento(vencimiento);
            nueva.setUsuario(usuario);
            medallaRepository.save(nueva);
        }
    }

    private void evaluarMedallasPorPuntaje(Usuario usuario) {
        int puntos = usuario.getPuntos();
        MedallaEnum tipo = null;

        if (puntos >= 100) {
            tipo = MedallaEnum.RESCATISTA_NIVEL_3;
        } else if (puntos >= 60) {
            tipo = MedallaEnum.RESCATISTA_NIVEL_2;
        } else if (puntos >= 30) {
            tipo = MedallaEnum.RESCATISTA_NIVEL_1;
        }

        if (tipo == null) return; // No califica para ninguna medalla

        List<Medalla> medallas = medallaRepository.findByUsuarioId(usuario.getId());
        Optional<Medalla> medallaExistente = medallas.stream()
                .filter(m -> m.getTipo().name().startsWith("RESCATISTA"))
                .findFirst();

        if (medallaExistente.isPresent()) {
            Medalla medalla = medallaExistente.get();
            // Si ya tiene una medalla de colaborador, actualizala solo si es de menor nivel
            if (medalla.getTipo().compareTo(tipo) < 0) {
                medalla.setTipo(tipo);
                medallaRepository.save(medalla);
            }
        } else {
            // Crear nueva si no tiene
            Medalla nueva = new Medalla();
            nueva.setTipo(tipo);
            nueva.setFechaAsignacion(new Date());
            nueva.setUsuario(usuario);
            medallaRepository.save(nueva);
        }
    }
}
