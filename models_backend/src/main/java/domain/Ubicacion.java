package domain;

import jakarta.persistence.*;

import java.util.Locale;

@Entity
public class Ubicacion  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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