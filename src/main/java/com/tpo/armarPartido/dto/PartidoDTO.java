package com.tpo.armarPartido.dto;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import java.util.Date;
import java.util.List;

public class PartidoDTO {
    private Deporte deporte;
    private int cantidadJugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private Date horario;
    private String estado;
    private Nivel nivel;
    private List<UsuarioDTO> jugadoresParticipan;
    private Long id;

    public PartidoDTO() {}

    public PartidoDTO(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario, String estado, Nivel nivel, List<UsuarioDTO> jugadoresParticipan) {
        this.deporte = deporte;
        this.cantidadJugadores = cantidadJugadores;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.horario = horario;
        this.estado = estado;
        this.nivel = nivel;
        this.jugadoresParticipan = jugadoresParticipan;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<UsuarioDTO> getJugadoresParticipan() {
        return jugadoresParticipan;
    }

    public void setJugadoresParticipan(List<UsuarioDTO> jugadoresParticipan) {
        this.jugadoresParticipan = jugadoresParticipan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
} 