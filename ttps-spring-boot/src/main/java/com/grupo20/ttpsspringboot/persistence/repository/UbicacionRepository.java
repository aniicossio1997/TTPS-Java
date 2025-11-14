package com.grupo20.ttpsspringboot.persistence.repository;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    // 1) Spring Data infiere este método solo por el nombre
    List<Ubicacion> findByProvincia(String provincia);

    // 2) Versión con filtro flexible usando @Query (ejemplo)
    @Query("""
           SELECT u
           FROM Ubicacion u
           WHERE (:idExterno IS NULL OR u.idExterno LIKE %:idExterno%)
             AND (:provincia IS NULL OR u.provincia LIKE %:provincia%)
             AND (:ciudad IS NULL OR u.ciudad LIKE %:ciudad%)
             AND (:barrio IS NULL OR u.barrio LIKE %:barrio%)
           """)
    List<Ubicacion> findByCriteriaLike(@Param("idExterno") String idExterno,
                                       @Param("provincia") String provincia,
                                       @Param("ciudad") String ciudad,
                                       @Param("barrio") String barrio);


}
