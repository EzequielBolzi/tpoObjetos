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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tpo.armarPartido.exception.JugadorYaParticipaException;

@Entity
public class Partido {
    private static final Logger logger = LoggerFactory.getLogger(Partido.class);

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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creador_id")
    private Usuario creadorUsuario;

    public Partido() {}

    public Partido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario,
                   EstadoPartido estado, EstrategiaEmparejamiento emparejamiento, List<Usuario> jugadoresParticipan,
                   Nivel nivel, Usuario creador) {
        this.deporte = deporte;
        this.cantidadJugadores = cantidadJugadores;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
        this.horario = horario;
        this.estado = estado;
        if (estado != null) {
            this.estadoNombre = estado.toString();
        }
        this.emparejamiento = emparejamiento;
        this.jugadoresParticipan = jugadoresParticipan;
        this.nivel = nivel;
        if (emparejamiento instanceof EmparejamientoPorUbicacion) {
            this.emparejamientoTipo = "ubicacion";
        } else if (emparejamiento instanceof EmparejamientoPorNivel) {
            this.emparejamientoTipo = "nivel";
        }
        this.creadorUsuario = creador;
    }

    public Partido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario,
                   EstadoPartido estado, EstrategiaEmparejamiento emparejamiento, List<Usuario> jugadoresParticipan,
                   Nivel nivel, List<com.tpo.armarPartido.service.iObserver> observers, Usuario creador) {
        this(deporte, cantidadJugadores, duracion, ubicacion, horario, estado, emparejamiento, jugadoresParticipan, nivel, creador);
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
        this.estadoNombre = nuevo.toString();
        logger.info("Cambiando estado de {} a {}", estadoAnterior, nuevo);
        notifyObservers();
        //Guarda el estado en la base de datos
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
        if (jugador == null) {
            logger.error("No se puede agregar un jugador nulo");
            throw new IllegalArgumentException("El jugador no puede ser nulo");
        }
        
        // Verificar si el jugador ya está en el partido
        if (esParticipante(jugador)) {
            logger.warn("El jugador {} ya participa en el partido {}", jugador.getNombre(), this.id);
            throw new JugadorYaParticipaException(jugador.getNombre(), this.id);
        }
        
        // Verificar si hay espacio en el partido
        if (this.jugadoresParticipan.size() >= this.cantidadJugadores) {
            logger.warn("El equipo está completo. No se puede agregar a {}", jugador.getNombre());
            throw new IllegalStateException("El partido ya tiene el máximo de jugadores permitidos");
        }
        
        // Agregar el jugador
        jugadoresParticipan.add(jugador);
        logger.info("Jugador {} agregado al partido {}. Total ahora: {}", 
                   jugador.getNombre(), this.id, jugadoresParticipan.size());
    }

    public void emparejarJugadores() {
        logger.debug("Emparejando jugadores con estrategia: {}", emparejamiento);
        if (emparejamiento == null) {
            logger.error("No se definió una estrategia de emparejamiento");
            throw new IllegalStateException("No se definió una estrategia de emparejamiento");
        }
        this.jugadoresParticipan = emparejamiento.emparejar(this, this.jugadoresParticipan);
        logger.info("Jugadores emparejados: {}", jugadoresParticipan);
    }

    public void comentar(Usuario jugador, String mensaje) {
        logger.info("{} comenta en el partido: {}", jugador, mensaje);
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
        if (jugador == null || jugador.getCorreo() == null) {
            logger.warn("esParticipante: jugador o correo es null");
            return false;
        }
        
        if (jugadoresParticipan == null) {
            logger.warn("esParticipante: lista de jugadores es null");
            return false;
        }
        
        logger.debug("esParticipante: verificando jugador {} (correo: {}) en partido {} con {} jugadores", 
                    jugador.getNombre(), jugador.getCorreo(), this.id, jugadoresParticipan.size());
        
        boolean res = false;
        for (Usuario usuario : jugadoresParticipan) {
            if (usuario == null || usuario.getCorreo() == null) {
                logger.warn("esParticipante: usuario en lista es null o sin correo, saltando");
                continue;
            }
            
            logger.debug("esParticipante: comparando correo {} con {}", 
                        jugador.getCorreo(), usuario.getCorreo());
            
            // Comparar por correo (case-insensitive)
            if (jugador.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                res = true;
                logger.debug("esParticipante: MATCH encontrado por correo");
                break;
            }
        }
        
        logger.debug("esParticipante: {} -> {}", jugador.getNombre(), res);
        return res;
    }

    public void setJugadoresParticipan(List<Usuario> jugadoresParticipan) {
        this.jugadoresParticipan = jugadoresParticipan;
    }
    
    public Usuario getCreador() {
        return this.creadorUsuario;
    }

    public boolean esCreador(Usuario jugador) {
        if (this.creadorUsuario == null || jugador == null || jugador.getId() == null) {
            logger.error("esCreador check failed due to nulls. Creador is null: {}. Jugador is null: {}. Jugador ID is null: {}",
                this.creadorUsuario == null,
                jugador == null,
                jugador != null && jugador.getId() == null);
            return false;
        }
        boolean esElCreador = this.creadorUsuario.getId().equals(jugador.getId());
        logger.info("Checking esCreador: Partido ID={}, Creador in DB ID={}, Jugador to check ID={}. Match: {}",
            this.id,
            this.creadorUsuario.getId(),
            jugador.getId(),
            esElCreador);
        return esElCreador;
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

    public void setCreador(Usuario creador) {
        this.creadorUsuario = creador;
    }
}
