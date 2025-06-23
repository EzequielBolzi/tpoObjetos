package com.tpo.armarPartido.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

import com.tpo.armarPartido.dto.PartidoDTO;
import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.*;
import com.tpo.armarPartido.model.*;
import com.tpo.armarPartido.repository.UsuarioRepository;
import com.tpo.armarPartido.service.*;
import com.tpo.armarPartido.service.estados.*;
import utils.*;
import com.tpo.armarPartido.repository.PartidoRepository;
import com.tpo.armarPartido.repository.ComentarioRepository;
import com.tpo.armarPartido.repository.NotificacionRepository;

public class ControllerPartido {
	
    private final PartidoRepository partidoRepository;
    private final ComentarioRepository comentarioRepository;
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private AdapterMail adapterMail;
    private NotificacionService notificacionService;
    private NotificadorMail notificadorMail;
    private NotificadorSMS notificadorSMS;

    public ControllerPartido() {
        this.usuarioRepository = new UsuarioRepository();
        this.partidoRepository = new PartidoRepository();
        this.comentarioRepository = new ComentarioRepository();
        this.notificacionRepository = new NotificacionRepository();
        this.adapterMail = new AdapterMail(
            "uadetpoturnonoche@gmail.com", // correo de origen para notificaciones
            "uadetpoturnonoche@gmail.com",
            "lkez jakv inbj epxg"
        );
        this.notificadorMail = new NotificadorMail(adapterMail);
        this.notificadorSMS = new NotificadorSMS();
        this.notificacionService = new NotificacionService(
            java.util.Arrays.asList(notificadorMail, notificadorSMS)
        );
    }

    public void crearPartido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario, EstrategiaEmparejamiento emparejamiento, Usuario usuarioCreador, Nivel nivel) {
        EstadoPartido estadoInicial = new NecesitamosJugadores();
        List<Usuario> listaJugadoresParticipan = new ArrayList<Usuario>();
        listaJugadoresParticipan.add(usuarioCreador);
        List<iObserver> observadores = new ArrayList<>();
        observadores.add(notificadorMail);
        Partido nuevo = new Partido(deporte, cantidadJugadores, duracion, ubicacion, horario, estadoInicial, emparejamiento, listaJugadoresParticipan, nivel, observadores, usuarioCreador);
        partidoRepository.save(nuevo);
        nuevo.cambiarEstado(estadoInicial);
        if (emparejamiento instanceof EmparejamientoPorUbicacion) {
            nuevo.setEmparejamientoTipo("ubicacion");
        } else if (emparejamiento instanceof EmparejamientoPorNivel) {
            nuevo.setEmparejamientoTipo("nivel");
        }
        partidoRepository.save(nuevo);
        System.out.println("Se creó un nuevo partido de " + nuevo.getDeporte());
    }
    
    public List<Partido> buscarMasCercanos(List<Partido> listaPartidos, Ubicacion ubicacionCentral, int cantidadPartidos) {
        if (ubicacionCentral == null) {
            throw new IllegalArgumentException("La ubicación central no puede ser nula");
        }
        if (listaPartidos == null || listaPartidos.isEmpty()) {
            return Collections.emptyList();
        }
        // Ordena los partidos por cercanía a la ubicación central
        List<Partido> partidosCercanos = listaPartidos.stream()
                .filter(p -> p.getUbicacion() != null)
                .sorted(new PartidoPorDistanciaComparator(ubicacionCentral))
                .limit(cantidadPartidos)
                .collect(Collectors.toList());
        System.out.println("Ubicación actual del jugador: " + ubicacionCentral.getLatitud() + ", " + ubicacionCentral.getLongitud());
        utilsPartido.printPartidos(partidosCercanos);
        return partidosCercanos;
    }
    
    public Partido getPartidoPorID(Long id) {
    	return partidoRepository.findById(id);
    }
    
    public void agregarJugadorAPartido(Long idPartido, Usuario jugadorNuevo) {
        Partido partido = getPartidoPorID(idPartido);
        if (partido != null) {
            partido.agregarJugador(jugadorNuevo);
            partidoRepository.save(partido);
            System.out.println("Se agregó el jugador " + jugadorNuevo + " al partido de " + partido.getDeporte() + " (nivel: " + partido.getNivel() + ")");
            System.out.println("-----------------------------------");
        }
    }
    
    public void printEstadoPartidoID (Long id) {
    	Partido partido = getPartidoPorID(id);
    	if (partido != null) {
    		System.out.println(partido.getEstado()); 
    	}
    }
    
    public Partido getPartidoID(Long id) {
    	return getPartidoPorID(id);
    }
    
    // Metodos de estado
        public void armarPartido(Long id) {
        Partido partido = getPartidoPorID(id);
        if (partido != null && partido.getEmparejamiento() != null) {
            // Restaura los observadores después de cargar desde la base de datos
            partido.resetObservers(java.util.Collections.singletonList(notificadorMail));
            List<Usuario> todosLosUsuarios = usuarioRepository.findAll();
            List<Usuario> seleccionados = partido.getEmparejamiento().emparejar(partido, todosLosUsuarios);
            partido.setJugadoresParticipan(seleccionados);
            if (partido.getJugadoresParticipan().size() >= partido.getCantidadJugadores()) {
                partido.getEstado().armar(partido);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaStr = partido.getHorario() != null ? new java.sql.Timestamp(partido.getHorario().getTime()).toLocalDateTime().format(formatter) : "fecha no especificada";
                for (Usuario usuario : partido.getJugadoresParticipan()) {
                    String msg = String.format(
                        "Hola %s! El partido de %s ha sido armado y está listo para confirmar.\nFecha y hora: %s\n%s\nPor favor, confirma tu participación.",
                        usuario.getNombre(),
                        partido.getDeporte(),
                        fechaStr,
                        getGoogleMapsAnchor(partido.getUbicacion())
                    );
                    notificacionService.notificarPorMedio(msg, usuario);
                    System.out.println("Notificación enviada a: " + usuario.getCorreo());
                }
            } else {
                System.out.println("No se pudo armar el partido: faltan jugadores online (seleccionados: " + seleccionados.size() + ")");
            }
            partidoRepository.save(partido);
            Partido refreshed = partidoRepository.findById(partido.getId());
            System.out.println("Estado del partido en base de datos tras guardar: " + (refreshed != null ? refreshed.getEstadoNombre() : "null"));
            System.out.println("-----------------------------------");
        } else {
            System.out.println("No se pudo armar el partido: partido o emparejamiento nulo para id: " + id);
        }
    }

    public void confirmarPartido(Long id, Usuario jugador) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            EstadoPartido estadoActual = partido.getEstado();
            if(partido.esParticipante(jugador)) {
                estadoActual.confirmar(partido);
                estadoActual.getMessage(partido);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaStr = partido.getHorario() != null ? new java.sql.Timestamp(partido.getHorario().getTime()).toLocalDateTime().format(formatter) : "fecha no especificada";
                if ("Confirmacion".equalsIgnoreCase(partido.getEstadoNombre())) {
                    for (Usuario usuario : partido.getJugadoresParticipan()) {
                        String msg = String.format(
                            "Hola %s! El partido de %s ha sido confirmado para el %s.\n%s\n¡Prepárate para jugar!",
                            usuario.getNombre(),
                            partido.getDeporte(),
                            fechaStr,
                            getGoogleMapsAnchor(partido.getUbicacion())
                        );
                        notificacionService.notificarPorMedio(msg, usuario);
                    }
                } else {
                    int faltan = partido.getJugadoresParticipan().size() - partido.getConfirmaciones();
                    for (Usuario usuario : partido.getJugadoresParticipan()) {
                        String msg = String.format(
                            "Hola %s! Faltan %d confirmaciones para confirmar el partido de %s.\nFecha y hora: %s\n%s",
                            usuario.getNombre(),
                            faltan,
                            partido.getDeporte(),
                            fechaStr,
                            getGoogleMapsAnchor(partido.getUbicacion())
                        );
                        notificacionService.notificarPorMedio(msg, usuario);
                    }
                }
                partidoRepository.save(partido);
            }
            else {
                System.err.println("El usuario " + jugador.getNombre()+ " que intenta confirmar no es parte del partido");
            }
        }
    }
    
    public void comenzarPartido(Long id, Usuario jugador, boolean overrideHorario) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            EstadoPartido estadoActual = partido.getEstado();
            if (partido.esCreador(jugador)) {
                if (overrideHorario && estadoActual instanceof com.tpo.armarPartido.service.estados.Confirmacion) {
                    partido.cambiarEstado(new com.tpo.armarPartido.service.estados.EnJuego());
                    System.out.println("El partido ha comenzado (override horario).");
                } else {
                    estadoActual.comenzar(partido);
                    estadoActual.getMessage(partido);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaStr = partido.getHorario() != null ? new java.sql.Timestamp(partido.getHorario().getTime()).toLocalDateTime().format(formatter) : "fecha no especificada";
                String msg = String.format(
                    "Hola %s! El partido de %s ha comenzado!\nFecha y hora: %s\n%s\n¡Mucha suerte!",
                    jugador.getNombre(),
                    partido.getDeporte(),
                    fechaStr,
                    getGoogleMapsAnchor(partido.getUbicacion())
                );
                for (Usuario usuario : partido.getJugadoresParticipan()) {
                    notificacionService.notificarPorMedio(msg, usuario);
                }
                partidoRepository.save(partido);
            } else {
                System.err.println("El usuario " + jugador.getNombre() + " que intenta comenzar no es el creador del partido");
            }
        }
    }
    
    public void finalizarPartido(Long id, Usuario jugador, boolean overrideHorario) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            EstadoPartido estadoActual = partido.getEstado();
            if (partido.esCreador(jugador)) {
                if (overrideHorario && estadoActual instanceof com.tpo.armarPartido.service.estados.EnJuego) {
                    partido.cambiarEstado(new com.tpo.armarPartido.service.estados.Finalizado());
                    System.out.println("El partido ha finalizado (override horario).");
                } else {
                    estadoActual.finalizar(partido);
                    estadoActual.getMessage(partido);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaStr = partido.getHorario() != null ? new java.sql.Timestamp(partido.getHorario().getTime()).toLocalDateTime().format(formatter) : "fecha no especificada";
                String msg = String.format(
                    "Hola %s! El partido de %s ha finalizado.\nFecha y hora: %s\n%s\n¡Gracias por participar!",
                    jugador.getNombre(),
                    partido.getDeporte(),
                    fechaStr,
                    getGoogleMapsAnchor(partido.getUbicacion())
                );
                for (Usuario usuario : partido.getJugadoresParticipan()) {
                    notificacionService.notificarPorMedio(msg, usuario);
                }
                partidoRepository.save(partido);
            } else {
                System.err.println("El usuario " + jugador.getNombre() + " que intenta finalizar no es el creador del partido");
            }
        }
    }

    // Sobrecargas para compatibilidad con versiones anteriores
    public void comenzarPartido(Long id, Usuario jugador) {
        comenzarPartido(id, jugador, false);
    }

    public void finalizarPartido(Long id, Usuario jugador) {
        finalizarPartido(id, jugador, false);
    }

    public List<PartidoDTO> getPartidosDTO() {
        return partidoRepository.findAll().stream().map(DTOMapper::toPartidoDTO).collect(Collectors.toList());
    }

    public PartidoDTO getPartidoDTOPorID(Long id) {
        Partido partido = getPartidoPorID(id);
        return partido != null ? DTOMapper.toPartidoDTO(partido) : null;
    }

    public void comentarPartido(Long id, Usuario jugador, String comentario) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            EstadoPartido estadoActual = partido.getEstado();
            Usuario jugadorDB = usuarioRepository.findByCorreo(jugador.getCorreo());
            if (jugadorDB == null) {
                System.err.println("No se encontró el usuario para comentar: " + jugador.getCorreo());
                return;
            }
            if(estadoActual.toString().equalsIgnoreCase("Finalizado")) {
                if(partido.esParticipante(jugador)) {
                    partido.comentar(jugador, comentario);
                    partidoRepository.save(partido);

                    Comentario nuevoComentario = new Comentario(jugadorDB, comentario, partido);
                    comentarioRepository.save(nuevoComentario);
                    System.out.println("El jugador " + jugadorDB.getNombre() + " dejó el siguiente comentario: " + comentario);
                }
            }
        }
    }

    public List<Partido> filtrarPorNivel(Nivel nivel) {
        return partidoRepository.findAll().stream()
            .filter(p -> p.getNivel().equals(nivel))
            .collect(Collectors.toList());
    }

    public List<Partido> filtrarPorUbicacion(double lat, double lng, int cantidad) {
        Ubicacion ubicacionCentral = new Ubicacion(lat, lng);
        List<Partido> partidos = partidoRepository.findAll();
        return buscarMasCercanos(partidos, ubicacionCentral, cantidad);
    }

    public List<Partido> filtrarPorNivelYUbicacion(Nivel nivel, double lat, double lng, int cantidad) {
        List<Partido> partidosPorNivel = filtrarPorNivel(nivel);
        Ubicacion ubicacionCentral = new Ubicacion(lat, lng);
        return buscarMasCercanos(partidosPorNivel, ubicacionCentral, cantidad);
    }

    public List<PartidoDTO> buscarPartidos(
        Nivel nivel,
        Double lat,
        Double lng,
        Integer cantidad
    ) {
        List<Partido> partidos;

        if (nivel != null && lat != null && lng != null && cantidad != null) {
            partidos = filtrarPorNivelYUbicacion(nivel, lat, lng, cantidad);
        } else if (nivel != null) {
            partidos = filtrarPorNivel(nivel);
        } else if (lat != null && lng != null && cantidad != null) {
            partidos = filtrarPorUbicacion(lat, lng, cantidad);
        } else {
            partidos = partidoRepository.findAll();
        }

        return partidos.stream().map(DTOMapper::toPartidoDTO).collect(Collectors.toList());
    }

    public void crearPartido(PartidoDTO partidoDTO, String emparejamiento) {
        if (partidoDTO.getJugadoresParticipan() == null || partidoDTO.getJugadoresParticipan().isEmpty()) {
            return;
        }
        // Obtiene las entidades Usuario gestionadas desde la base de datos
        List<Usuario> jugadores = new ArrayList<>();
        for (UsuarioDTO jugadorDTO : partidoDTO.getJugadoresParticipan()) {
            Usuario usuario = new com.tpo.armarPartido.repository.UsuarioRepository().findByCorreo(jugadorDTO.getCorreo());
            if (usuario != null) {
                jugadores.add(usuario);
            } else {
                // Si el usuario no se encuentra, se puede manejar el error o simplemente omitirlo
                // No se maneja el error porque se asume que el usuario ya existe
            }
        }
        Usuario usuarioCreador = jugadores.get(0); 
        // El primer usuario de la lista es considerado el creador del partido
        EstrategiaEmparejamiento estrategia;
        if (emparejamiento.equalsIgnoreCase("ubicacion")) {
            estrategia = new EmparejamientoPorUbicacion();
        } else if (emparejamiento.equalsIgnoreCase("nivel")) {
            estrategia = new EmparejamientoPorNivel();
        } else {
            return;
        }
        EstadoPartido estadoInicial = new NecesitamosJugadores();
        crearPartido(
            partidoDTO.getDeporte(),
            partidoDTO.getCantidadJugadores(),
            partidoDTO.getDuracion(),
            partidoDTO.getUbicacion(),
            partidoDTO.getHorario(),
            estrategia,
            usuarioCreador,
            partidoDTO.getNivel(),
            jugadores
        );
    }

    public void eliminarPartido(Long id) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            partidoRepository.delete(partido);
        }
    }

    public void agregarJugador(Long id, UsuarioDTO usuarioDTO) {
        Usuario jugador = new com.tpo.armarPartido.repository.UsuarioRepository().findByCorreo(usuarioDTO.getCorreo());
        if (jugador == null) {
            System.err.println("No se encontró el usuario: " + usuarioDTO.getCorreo());
            return;
        }
        Partido partido = getPartidoPorID(id);
        if (partido == null) return;
        agregarJugadorAPartido(id, jugador);
    }

    public void armar(Long id) {
        Partido partido = getPartidoPorID(id);
        if (partido == null) return;
        armarPartido(id);
    }

    public void confirmar(Long id, UsuarioDTO usuarioDTO) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return;
        confirmarPartido(id, jugador);
    }

    public void comenzar(Long id, UsuarioDTO usuarioDTO, boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        
        if (partido != null && partido.getCreador() != null) {
            System.out.println("DEBUG: Partido ID=" + partido.getId() + ", Creador fetched from DB: ID=" + partido.getCreador().getId() + ", Nombre=" + partido.getCreador().getNombre());
        } else {
            System.out.println("DEBUG: Partido o creador es nulo al cargarlo. Partido: " + (partido != null ? "cargado" : "null") + ", Creador: " + (partido != null && partido.getCreador() != null ? "cargado" : "null"));
        }
        if (jugador != null) {
            System.out.println("DEBUG: Jugador from DTO: ID=" + jugador.getId() + ", Nombre=" + jugador.getNombre());
        } else {
            System.out.println("DEBUG: Jugador from DTO is null.");
        }

        if (partido == null) return;
        comenzarPartido(id, jugador, overrideHorario);
    }

    public void finalizar(Long id, UsuarioDTO usuarioDTO, boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return;
        finalizarPartido(id, jugador, overrideHorario);
    }

    public void cancelarPartido(Long id, Usuario jugador) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            if (partido.esCreador(jugador)) {
                EstadoPartido estadoActual = partido.getEstado();
                estadoActual.cancelar(partido);
                
                // Enviar notificaciones a todos los participantes
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaStr = partido.getHorario() != null ? 
                    new java.sql.Timestamp(partido.getHorario().getTime()).toLocalDateTime().format(formatter) : 
                    "fecha no especificada";
                
                for (Usuario usuario : partido.getJugadoresParticipan()) {
                    String msg = String.format(
                        "Hola %s! El partido de %s programado para el %s ha sido CANCELADO por el organizador.\n%s\nDisculpa las molestias.",
                        usuario.getNombre(),
                        partido.getDeporte(),
                        fechaStr,
                        getGoogleMapsAnchor(partido.getUbicacion())
                    );
                    notificacionService.notificarPorMedio(msg, usuario);
                }
                
                partidoRepository.save(partido);
                System.out.println("Partido " + id + " cancelado por " + jugador.getNombre());
            } else {
                System.err.println("El usuario " + jugador.getNombre() + " que intenta cancelar no es el creador del partido");
                throw new RuntimeException("Solo el creador del partido puede cancelarlo");
            }
        } else {
            System.err.println("Partido no encontrado con ID: " + id);
            throw new RuntimeException("Partido no encontrado");
        }
    }

    public void cancelar(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findByCorreo(usuarioDTO.getCorreo());
        if (usuario != null) {
            cancelarPartido(id, usuario);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    public void comentar(Long id, ComentarioRequest comentarioRequest) {
        Usuario jugador = DTOMapper.toUsuario(comentarioRequest.getUsuario(), "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return;
        comentarPartido(id, jugador, comentarioRequest.getComentario());
    }

    public static class ComentarioRequest {
        private UsuarioDTO usuario;
        private String comentario;
        public UsuarioDTO getUsuario() { return usuario; }
        public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }

    public void crearPartido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario, EstrategiaEmparejamiento emparejamiento, Usuario usuarioCreador, Nivel nivel, List<Usuario> jugadoresParticipan) {
        List<iObserver> observadores = new ArrayList<>();
        observadores.add(notificadorMail);
        EstadoPartido estadoInicial = new NecesitamosJugadores();
        Partido nuevo = new Partido(deporte, cantidadJugadores, duracion, ubicacion, horario, estadoInicial, emparejamiento, jugadoresParticipan, nivel, observadores, usuarioCreador);
        partidoRepository.save(nuevo);
        nuevo.cambiarEstado(estadoInicial);
        if (emparejamiento instanceof EmparejamientoPorUbicacion) {
            nuevo.setEmparejamientoTipo("ubicacion");
        } else if (emparejamiento instanceof EmparejamientoPorNivel) {
            nuevo.setEmparejamientoTipo("nivel");
        }
        partidoRepository.save(nuevo);
        System.out.println(" + Se creo un nuevo partido de " + nuevo.getDeporte());
    }

    private String getGoogleMapsAnchor(Ubicacion ubicacion) {
        if (ubicacion == null) return "Ubicación no especificada";
        String url = String.format("https://www.google.com/maps/search/?api=1&query=%s,%s", ubicacion.getLatitud(), ubicacion.getLongitud());
        return String.format("Ubicación: %s", url);
    }
}
