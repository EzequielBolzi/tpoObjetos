package com.tpo.armarPartido.dto;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import java.util.Map;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String correo;
    private Map<Deporte, Nivel> nivelesPorDeporte;
    private MedioNotificacion medioNotificacion;
    private Ubicacion ubicacion;

    public UsuarioDTO() {}

    public UsuarioDTO(String id, String nombre, String correo, Map<Deporte, Nivel> nivelesPorDeporte, MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        this.id = Long.parseLong(id);
        this.nombre = nombre;
        this.correo = correo;
        this.nivelesPorDeporte = nivelesPorDeporte;
        this.medioNotificacion = medioNotificacion;
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Map<Deporte, Nivel> getNivelesPorDeporte() {
        return nivelesPorDeporte;
    }

    public void setNivelesPorDeporte(Map<Deporte, Nivel> nivelesPorDeporte) {
        this.nivelesPorDeporte = nivelesPorDeporte;
    }

    public MedioNotificacion getMedioNotificacion() {
        return medioNotificacion;
    }

    public void setMedioNotificacion(MedioNotificacion medioNotificacion) {
        this.medioNotificacion = medioNotificacion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
} 