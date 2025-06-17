package com.tpo.armarPartido.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;

import java.util.Map;

@Document(collection = "usuarios")
public class Usuario {
	@Id
	private String id;
	private String nombre;
	private String correo;
	private String contrasena;
	private Map<Deporte, Nivel> nivelesPorDeporte;
	private MedioNotificacion medioNotificacion;
	private Ubicacion ubicacion;

	public Usuario(String nombre, String correo, String contrasena,
				   Map<Deporte, Nivel> nivelesPorDeporte,
				   MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
		this.nombre = nombre;
		this.correo = correo;
		this.contrasena = contrasena;
		this.nivelesPorDeporte = nivelesPorDeporte;
		this.medioNotificacion = medioNotificacion;
		this.ubicacion = ubicacion;
	}

	// Getters y Setters
	public String getNombre() {
		return nombre;
	}

	public String getCorreo() {
		return correo;
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

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", correo=" + correo + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Usuario usuario = (Usuario) o;
		return correo != null && correo.equalsIgnoreCase(usuario.correo);
	}

	@Override
	public int hashCode() {
		return correo != null ? correo.toLowerCase().hashCode() : 0;
	}
}
