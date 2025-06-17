package com.tpo.armarPartido.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.tpo.armarPartido.dto.PartidoDTO;
import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.*;
import com.tpo.armarPartido.model.*;
import com.tpo.armarPartido.service.*;
import com.tpo.armarPartido.service.estados.*;
import utils.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.tpo.armarPartido.repository.PartidoRepository;

@RestController
@RequestMapping("/api/partidos")
public class ControllerPartido {
	
    @Autowired
    private PartidoRepository partidoRepository;
    private static ControllerPartido instancia;

    private ControllerPartido() {
    }

    public static ControllerPartido getInstancia() {
        System.out.println("Inicio Controlador de Partidos ");
    	if (instancia == null) {
            instancia = new ControllerPartido();
        }
        return instancia;
    }
    
    public void crearPartido(Deporte deporte, int cantidadJugadores, int duracion, Ubicacion ubicacion, Date horario, EstrategiaEmparejamiento emparejamiento, Usuario usuarioCreador, Nivel nivel) {
    	EstadoPartido estadoInicial = new NecesitamosJugadores();
    	List<Usuario> listaJugadoresParticipan = new ArrayList<Usuario>();
    	listaJugadoresParticipan.add(usuarioCreador);
        AdapterNotificacionMail adapter = new AdapterMail(); // agrego el adaptermail
        Notificador notificador = new Notificador(adapter);
    	List<iObserver> observadores = new ArrayList<iObserver>();
    	observadores.add(notificador);
    	Partido nuevo = new Partido(deporte, cantidadJugadores, duracion, ubicacion, horario, estadoInicial, emparejamiento, listaJugadoresParticipan, nivel, observadores);
    	partidoRepository.save(nuevo);
    	System.out.println(" + Se creo un nuevo partido de " + nuevo.getDeporte());
    }
    
    public void buscarPartidosPorNivel(Nivel nivel) {
        System.out.println("Buscando partidos con nivel: " + nivel);
        List<Partido> partidos = partidoRepository.findAll();
        boolean encontrados = false;
        for (Partido partido : partidos) {
            if (partido.getNivel().equals(nivel)) {
                System.out.println("ID Partido: " + partido.getId());
                System.out.println(" - Deporte: " + partido.getDeporte());
                System.out.println(" - Nivel: " + partido.getNivel());
                System.out.println(" - Ubicación: " + partido.getUbicacion());
                System.out.println(" - Horario: " + partido.getHorario());
                System.out.println(" - Cantidad de jugadores: " + partido.getCantidadJugadores());
                System.out.println(" - Jugadores actuales: " + partido.getJugadoresParticipan().size());
                System.out.println(" - Estado: " + partido.getEstado());
                System.out.println("-----------------------------------");
                encontrados = true;
            }
        }
        if (!encontrados) {
            System.out.println("No se encontraron partidos con el nivel: " + nivel);
        }
    }
    
    public void buscarPartidosPorUbicacion(Ubicacion ubicacionCentral, int cantidadPartidos) {
    	if(ubicacionCentral == null) {
    		System.err.println("La ubicacion esta vacia.");
    	}
    	
    	List<Partido> partidosEnEstadoNecesitamosJugadores = partidoRepository.findAll().stream()
            .filter(p -> p.getEstado().toString().equals("NecesitamosJugadores"))
            .collect(Collectors.toList());
    	
    	buscarMasCercanos(partidosEnEstadoNecesitamosJugadores, ubicacionCentral, cantidadPartidos);
    }
    
    public List<Partido> buscarMasCercanos(List<Partido> listaPartidos, Ubicacion ubicacionCentral, int cantidadPartidos) {
    	if (ubicacionCentral == null) {
            throw new IllegalArgumentException("La ubicación central no puede ser null");
        }
        if (listaPartidos == null || listaPartidos.isEmpty()) {
            return Collections.emptyList();
        }
        List<Partido> partidosCercanos = listaPartidos.stream()
                .filter(p -> p.getUbicacion() != null)
                .sorted(new PartidoPorDistanciaComparator(ubicacionCentral))
                .limit(cantidadPartidos)
                .collect(Collectors.toList());
        	System.out.println("La ubicacion actual del jugador es: " + ubicacionCentral.getLatitud() + " " + ubicacionCentral.getLongitud());
            utilsPartido.printPartidos(partidosCercanos);
            return partidosCercanos;
    }
    
    public Partido getPartidoPorID(String id) {
    	return partidoRepository.findById(id).orElse(null);
    }
    
    public void agregarJugadorAPartido(String idPartido, Usuario jugadorNuevo) {
    	Partido partido = getPartidoPorID(idPartido);
    	if (partido != null) {
    		partido.agregarJugador(jugadorNuevo);
    		partidoRepository.save(partido);
    		System.out.println("Se agrego jugador "+jugadorNuevo+" al partido: "+partido.getDeporte()+" del nivel: "+partido.getNivel());
    		System.out.println("-----------------------------------");
    	}
    }
    
    public void printEstadoPartidoID (String id) {
    	Partido partido = getPartidoPorID(id);
    	if (partido != null) {
    		System.out.println(partido.getEstado()); 
    	}
    }
    
    public Partido getPartidoID(String id) {
    	return getPartidoPorID(id);
    }
    
    // Metodos de estado
    
    public void armarPartido(String id) {
        Partido partido = getPartidoPorID(id);
        if (partido != null && partido.getEmparejamiento() != null) {
            System.out.println("🔄 Armando partido segun estrategia de emparejamiento...");
            List<Usuario> posiblesJugadores = partido.getJugadoresParticipan();
            List<Usuario> seleccionados = partido.getEmparejamiento().emparejar(partido, posiblesJugadores);
            partido.setJugadoresParticipan(seleccionados);
            if (partido.getJugadoresParticipan().size() >= partido.getCantidadJugadores()) {
                partido.getEstado().armar(partido);
                System.out.println("✅ Partido armado con éxito con estrategia: " + partido.getEmparejamiento().toString());
            } else {
                System.out.println("⚠️ No se pudo armar el partido. Jugadores seleccionados: " + seleccionados.size() + " --> falta de Jugadores Online");
            }
            partidoRepository.save(partido);
            System.out.println("-----------------------------------");
        }
    }
    
    public void confirmarPartido(String id, Usuario jugador) {
    	Partido partido = getPartidoPorID(id);
    	if (partido != null) {
    		EstadoPartido estadoActual = partido.getEstado();
    		if(partido.esParticipante(jugador)) {
	    		estadoActual.confirmar(partido);
	    		estadoActual.getMessage(partido);
	    		partidoRepository.save(partido);
	    	}
	    	else {
	    		System.err.println("El " + jugador.getNombre()+ " que intenta confirmar no es parte del Partido");
			}
    	}
    }
    
    public void comenzarPartido(String id, Usuario jugador, boolean overrideHorario) {
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
                partidoRepository.save(partido);
            } else {
                System.err.println("El " + jugador.getNombre() + " que intenta comenzar no es creador del partido");
            }
        }
    }
    
    public void finalizarPartido(String id, Usuario jugador, boolean overrideHorario) {
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
                partidoRepository.save(partido);
            } else {
                System.err.println("El " + jugador.getNombre() + " que intenta comenzar no es creador del partido");
            }
        }
    }

    // Overloads for backward compatibility
    public void comenzarPartido(String id, Usuario jugador) {
        comenzarPartido(id, jugador, false);
    }

    public void finalizarPartido(String id, Usuario jugador) {
        finalizarPartido(id, jugador, false);
    }

    public List<PartidoDTO> getPartidosDTO() {
        return partidoRepository.findAll().stream().map(DTOMapper::toPartidoDTO).collect(Collectors.toList());
    }

    public PartidoDTO getPartidoDTOPorID(String id) {
        Partido partido = getPartidoPorID(id);
        return partido != null ? DTOMapper.toPartidoDTO(partido) : null;
    }
    
    public void comentarPartido(String id, Usuario jugador, String comentario) {
    	Partido partido = getPartidoPorID(id);
    	if (partido != null) {
    		EstadoPartido estadoActual = partido.getEstado();
    		if(estadoActual.toString().equalsIgnoreCase("Finalizado")) {
    			if(partido.esParticipante(jugador)) {
    				partido.comentar(jugador, comentario);
    				partidoRepository.save(partido);
    				System.out.println("El jugador: " + jugador.getNombre() + " dejo el siguiente comentario: " + comentario);
    			}
    		}
    	}
    }

    @GetMapping
    public List<PartidoDTO> listarPartidos() {
        return getPartidosDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> obtenerPartido(@PathVariable String id) {
        PartidoDTO partido = getPartidoDTOPorID(id);
        if (partido != null) {
            return ResponseEntity.ok(partido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Void> crearPartido(@RequestBody PartidoDTO partidoDTO, @RequestParam String emparejamiento) {
        // Get creator from first jugador in DTO
        if (partidoDTO.getJugadoresParticipan() == null || partidoDTO.getJugadoresParticipan().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        UsuarioDTO creadorDTO = partidoDTO.getJugadoresParticipan().get(0);
        // For now, password is not handled (should be improved)
        Usuario usuarioCreador = DTOMapper.toUsuario(creadorDTO, "");

        EstrategiaEmparejamiento estrategia;
        if (emparejamiento.equalsIgnoreCase("ubicacion")) {
            estrategia = new EmparejamientoPorUbicacion();
        } else if (emparejamiento.equalsIgnoreCase("nivel")) {
            estrategia = new EmparejamientoPorNivel();
        } else {
            return ResponseEntity.badRequest().build();
        }

        crearPartido(
            partidoDTO.getDeporte(),
            partidoDTO.getCantidadJugadores(),
            partidoDTO.getDuracion(),
            partidoDTO.getUbicacion(),
            partidoDTO.getHorario(),
            estrategia,
            usuarioCreador,
            partidoDTO.getNivel()
        );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPartido(@PathVariable String id) {
        Partido partido = getPartidoPorID(id);
        if (partido != null) {
            partidoRepository.delete(partido);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/agregar-jugador")
    public ResponseEntity<Void> agregarJugador(@PathVariable String id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        agregarJugadorAPartido(id, jugador);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/armar")
    public ResponseEntity<Void> armar(@PathVariable String id) {
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        armarPartido(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable String id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        confirmarPartido(id, jugador);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comenzar")
    public ResponseEntity<Void> comenzar(@PathVariable String id, @RequestBody UsuarioDTO usuarioDTO,
                                         @RequestParam(defaultValue = "false") boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        comenzarPartido(id, jugador, overrideHorario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable String id, @RequestBody UsuarioDTO usuarioDTO,
                                          @RequestParam(defaultValue = "false") boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        finalizarPartido(id, jugador, overrideHorario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comentar")
    public ResponseEntity<Void> comentar(@PathVariable String id, @RequestBody ComentarioRequest comentarioRequest) {
        Usuario jugador = DTOMapper.toUsuario(comentarioRequest.getUsuario(), "");
        Partido partido = getPartidoPorID(id);
        if (partido == null) return ResponseEntity.notFound().build();
        comentarPartido(id, jugador, comentarioRequest.getComentario());
        return ResponseEntity.ok().build();
    }

    // DTO for comment endpoint
    public static class ComentarioRequest {
        private UsuarioDTO usuario;
        private String comentario;
        public UsuarioDTO getUsuario() { return usuario; }
        public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
    }
}
