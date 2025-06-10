package com.tpo.armarPartido.dto;

import java.util.Date;
import java.util.List;

import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.enums.*;
import com.tpo.armarPartido.model.*;
import com.tpo.armarPartido.service.*;
import com.tpo.armarPartido.service.estados.EstadoPartido;


public class PartidoDTO {
    
    private Deporte deporte;
    private int cantidadJugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private Date horario;
    private EstadoPartido estado;
    private EstrategiaEmparejamiento emparejamiento;
    private List<UsuarioDTO> jugadores;
    private List<ComentarioDTO> comentarios;
	
    
    public PartidoDTO(Deporte deporte, int cantidadJugadores, int duracion,
			Ubicacion ubicacion, Date horario, EstadoPartido estado,
			EstrategiaEmparejamiento emparejamiento, List<UsuarioDTO> jugadores,
			List<ComentarioDTO> comentarios) {
		super();
		this.deporte = deporte;
		this.cantidadJugadores = cantidadJugadores;
		this.duracion = duracion;
		this.ubicacion = ubicacion;
		this.horario = horario;
		this.estado = estado;
		this.emparejamiento = emparejamiento;
		this.jugadores = jugadores;
		this.comentarios = comentarios;
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


	public EstadoPartido getEstado() {
		return estado;
	}


	public void setEstado(EstadoPartido estado) {
		this.estado = estado;
	}


	public EstrategiaEmparejamiento getEmparejamiento() {
		return emparejamiento;
	}


	public void setEmparejamiento(EstrategiaEmparejamiento emparejamiento) {
		this.emparejamiento = emparejamiento;
	}


	public List<UsuarioDTO> getJugadores() {
		return jugadores;
	}


	public void setJugadores(List<UsuarioDTO> jugadores) {
		this.jugadores = jugadores;
	}


	public List<ComentarioDTO> getComentarios() {
		return comentarios;
	}


	public void setComentarios(List<ComentarioDTO> comentarios) {
		this.comentarios = comentarios;
	}
    
    
}
