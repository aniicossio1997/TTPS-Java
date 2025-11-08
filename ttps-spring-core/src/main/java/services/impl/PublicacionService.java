package services.impl;

import domain.enums.RolUsuarioEnum;
import domain.models.EstadoPublicacion;
import domain.models.Publicacion;
import domain.models.Ubicacion;
import domain.models.Usuario;
import dtos.PublicacionCreateDTO;
import dtos.PublicacionUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.dao.PublicacionDAO;
import persistence.dao.UsuarioDAO;
import services.UbicacionService;

@Service
public class PublicacionService {
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private PublicacionDAO publicacionDAO;

    @Autowired
    private UbicacionService ubicacionService;

    @Transactional
    public Publicacion create(Usuario usuario, PublicacionCreateDTO dto) {
        Publicacion publicacion = dto.toEntity();
        publicacion.setUsuario(usuario);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        publicacion.setUbicacion(ubicacion);

        publicacionDAO.persist(publicacion);

        return publicacion;
    }


}
