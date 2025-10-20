package domain.models;

import jakarta.persistence.*;

@Entity // Indica que esta clase es una entidad persistente
public class Ubicacion  {

    @Id // Marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerado por la BD
    private Long id;
    private String idExterno;
    private String provincia;
    private String ciudad;
    private String barrio;
    private Double latitud;
    private Double longitud;


    public Ubicacion(String idExterno, String provincia, String ciudad, String barrio, Double latitud, Double longitud) {
        this.idExterno = idExterno;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.barrio = barrio;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Ubicacion() {

    }

    // --- getters ---

    public Long getId() {
        return id;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getBarrio() {
        return barrio;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }


}