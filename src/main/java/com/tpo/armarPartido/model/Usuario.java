package com.tpo.armarPartido.model;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import java.util.Map;
import jakarta.persistence.*;
import java.util.HashMap;

@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private String correo;
	private String contrasena;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "usuario_niveles", joinColumns = @JoinColumn(name = "usuario_id"))
	@MapKeyColumn(name = "deporte")
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name = "nivel")
	@Enumerated(EnumType.STRING)
	private Map<Deporte, Nivel> nivelesPorDeporte = new HashMap<>();

	@Enumerated(EnumType.STRING)
	private MedioNotificacion medioNotificacion;

	@Embedded
	private Ubicacion ubicacion;

	public Usuario() {}

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

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
