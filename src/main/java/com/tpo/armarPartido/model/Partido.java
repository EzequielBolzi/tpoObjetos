package com.tpo.armarPartido.model;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
import com.tpo.armarPartido.service.estados.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import java.util.HashSet;
import com.tpo.armarPartido.service.EmparejamientoPorUbicacion;
import com.tpo.armarPartido.service.EmparejamientoPorNivel;
import com.tpo.armarPartido.repository.NotificacionRepository;
import com.tpo.armarPartido.model.Notificacion;
import java.time.LocalDateTime;

@Entity
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Deporte deporte;
    private int cantidadJugadores;
    private int duracion;
    @Embedded
    private Ubicacion ubicacion;
    private Date horario;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "partido_jugadores",
        joinColumns = @JoinColumn(name = "partido_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> jugadoresParticipan = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Nivel nivel;
    @Transient
    private transient EstadoPartido estado;
    @Transient
    private transient EstrategiaEmparejamiento emparejamiento;
    @Transient
    private transient List<com.tpo.armarPartido.service.iObserver> observers = new ArrayList<>();
    private String estadoNombre;
    private String emparejamientoTipo; 
    private int confirmaciones;

    public Partido() {}

    public Partido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario,
                   EstadoPartido estado, EstrategiaEmparejamiento emparejamiento, List<Usuario> jugadoresParticipan,
                   Nivel nivel) {
        this.deporte = deporte;
        this.cantidadJugadores = cantidadJugadores;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.horario = horario;
        this.estado = estado;
        this.emparejamiento = emparejamiento;
        this.jugadoresParticipan = jugadoresParticipan;
        this.nivel = nivel;
        if (emparejamiento instanceof EmparejamientoPorUbicacion) {
            this.emparejamientoTipo = "ubicacion";
        } else if (emparejamiento instanceof EmparejamientoPorNivel) {
            this.emparejamientoTipo = "nivel";
        }
    }

    public Partido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario,
                   EstadoPartido estado, EstrategiaEmparejamiento emparejamiento, List<Usuario> jugadoresParticipan,
                   Nivel nivel, List<com.tpo.armarPartido.service.iObserver> observers) {
        this(deporte, cantidadJugadores, duracion, ubicacion, horario, estado, emparejamiento, jugadoresParticipan, nivel);
        if (observers != null) {
            this.observers = observers;
        }
    }

    @Override
	public String toString() {
		return "Partido [deporte=" + deporte + ", cantidadJugadores=" + cantidadJugadores + ", duracion=" + duracion
				+ ", ubicacion=" + ubicacion + ", horario=" + horario + ", estado=" + estado + ", emparejamiento="
				+ emparejamiento + ", jugadoresParticipan=" + jugadoresParticipan + ", nivel=" + nivel + "]";
	}

	public void cambiarEstado(EstadoPartido nuevo) {
        EstadoPartido estadoAnterior = this.estado;
        this.estado = nuevo;
        System.out.println("++ Nuevo estado: " + this.getEstado());
        System.out.println("-----------------------------------");
        notifyObservers();

        
        NotificacionRepository notificacionRepo = new NotificacionRepository();
        String mensaje = "El partido ha cambiado de estado a: " + nuevo.toString();
        String tipo = "ESTADO";
        LocalDateTime fechaEnvio = LocalDateTime.now();
        for (Usuario usuario : this.getJugadoresParticipan()) {
            Notificacion notificacion = new Notificacion(usuario, this, mensaje, tipo, fechaEnvio);
            notificacionRepo.save(notificacion);
        }
    }

    public void agregarJugador(Usuario jugador) {
        if (this.jugadoresParticipan.size() < this.cantidadJugadores) {
            jugadoresParticipan.add(jugador);
        } else {
            System.out.println("El equipo está completo.");
        }
    }

    public void emparejarJugadores() {
        if (emparejamiento == null) {
            throw new IllegalStateException("No se definió una estrategia de emparejamiento");
        }
        this.jugadoresParticipan = emparejamiento.emparejar(this, this.jugadoresParticipan);
    }

    public void comentar(Usuario jugador, String mensaje) {
        this.getEstado().comentar(jugador, mensaje, this);
    }

    public int getCantidadJugadores() {
        return this.cantidadJugadores;
    }

    public Date getHorario() {
        return this.horario;
    }

    public int getDuracion() {
        return this.duracion;
    }

    public List<Usuario> getJugadoresParticipan() {
        return this.jugadoresParticipan;
    }

    public Nivel getNivel() {
        return this.nivel;
    }

    public Ubicacion getUbicacion() {
        return this.ubicacion;
    }

    public Deporte getDeporte() {
        return this.deporte;
    }
    
    public EstadoPartido getEstado() {
    	return this.estado;
    }
    
    public EstrategiaEmparejamiento getEmparejamiento() {
        return this.emparejamiento;
    }

    public void setEmparejamiento(EstrategiaEmparejamiento emparejamiento) {
        this.emparejamiento = emparejamiento;
    }
    
    public boolean esParticipante(Usuario jugador) {
    	boolean res = false;
    	for(Usuario usuario: jugadoresParticipan) {
    		if(usuario.equals(jugador)) {
    			res = true;
    			break;
    		}
    	}
    	return res;
    }

    public void setJugadoresParticipan(List<Usuario> jugadoresParticipan) {
        this.jugadoresParticipan = jugadoresParticipan;
    }
    
    public String getCreadorPartido(Partido partido) {
    	return partido.getJugadoresParticipan().get(0).toString();
    }

    public boolean esCreador(Usuario jugador) {
    	boolean res = false;
    	int jugadorCreador = 0;
    	if (this.jugadoresParticipan.get(jugadorCreador).equals(jugador)) {
    		res = true;
    	}
    	return res;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public void setCantidadJugadores(int cantidadJugadores) {
        this.cantidadJugadores = cantidadJugadores;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addObserver(com.tpo.armarPartido.service.iObserver observer) {
        this.observers.add(observer);
    }

    public void removeObserver(com.tpo.armarPartido.service.iObserver observer) {
        this.observers.remove(observer);
    }

    private void notifyObservers() {
        for (com.tpo.armarPartido.service.iObserver observer : observers) {
            observer.actualizar(this);
        }
    }

    @PostLoad
    private void postLoad() {
        this.estado = estadoFromNombre(this.estadoNombre);
        if ("ubicacion".equalsIgnoreCase(this.emparejamientoTipo)) {
            this.emparejamiento = new EmparejamientoPorUbicacion();
        } else if ("nivel".equalsIgnoreCase(this.emparejamientoTipo)) {
            this.emparejamiento = new EmparejamientoPorNivel();
        }
    }

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
        this.estado = estadoFromNombre(estadoNombre);
    }

    public static EstadoPartido estadoFromNombre(String nombre) {
        if (nombre == null) return new NecesitamosJugadores();
        switch (nombre) {
            case "NecesitamosJugadores": return new NecesitamosJugadores();
            case "PartidoArmado": return new PartidoArmado();
            case "Confirmacion": return new Confirmacion();
            case "EnJuego": return new EnJuego();
            case "Finalizado": return new Finalizado();
            case "Cancelado": return new Cancelado();
            default: return new NecesitamosJugadores();
        }
    }

    public void resetObservers(List<com.tpo.armarPartido.service.iObserver> newObservers) {
        this.observers.clear();
        if (newObservers != null) {
            this.observers.addAll(newObservers);
        }
    }

    public List<com.tpo.armarPartido.service.iObserver> getObservers() {
        return this.observers;
    }

    public String getEmparejamientoTipo() {
        return emparejamientoTipo;
    }

    public void setEmparejamientoTipo(String emparejamientoTipo) {
        this.emparejamientoTipo = emparejamientoTipo;
    }

    public int getConfirmaciones() {
        return confirmaciones;
    }

    public void setConfirmaciones(int confirmaciones) {
        this.confirmaciones = confirmaciones;
    }
}
