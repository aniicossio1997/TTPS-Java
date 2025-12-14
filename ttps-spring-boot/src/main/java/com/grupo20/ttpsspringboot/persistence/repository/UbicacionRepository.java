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
          WHERE (:idExternoProvincia IS NULL OR u.idExternoProvincia LIKE %:idExternoProvincia%)
            AND (:provincia IS NULL OR u.provincia LIKE %:provincia%)
            AND (:idExternoMunicipio IS NULL OR u.idExternoMunicipio LIKE %:idExternoMunicipio%)
            AND (:municipio IS NULL OR u.municipio LIKE %:municipio%)
            AND (:idExternoDepartamento IS NULL OR u.idExternoDepartamento LIKE %:idExternoDepartamento%)
            AND (:departamento IS NULL OR u.departamento LIKE %:departamento%)
          """)
    List<Ubicacion> findByCriteriaLike(@Param("idExternoProvincia") String idExternoProvincia,
                                        @Param("provincia") String provincia,
                                        @Param("idExternoMunicipio") String idExternoMunicipio,
                                        @Param("municipio") String municipio,
                                        @Param("idExternoDepartamento") String idExternoDepartamento,
                                        @Param("departamento") String departamento
  );


}
