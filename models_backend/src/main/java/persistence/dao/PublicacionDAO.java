package persistence.dao;

import domain.models.Publicacion;

import java.util.List;


import java.util.Date;

public interface PublicacionDAO extends GenericDAO<Publicacion> {
    public List<Publicacion> getPublicacionesByNombre(String nombre);

    public List<Publicacion> getPublicacionesByUsuario(Long usuarioId);

    public List<Publicacion> getPublicacionesByCaracteristicas(
            String nombre,
            String especie,
            String raza,
            String tamanio,
            String color,
            java.util.Date fechaDesde,
            java.util.Date fechaHasta,
            int offset,
            int maxResults
    );
}