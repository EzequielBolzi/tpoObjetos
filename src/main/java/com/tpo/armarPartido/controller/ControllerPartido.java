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

@RestController
@RequestMapping("/api/partidos")
public class ControllerPartido {
	
    private static ControllerPartido instancia;
    private List<Partido> partidos;

    private ControllerPartido() {
    	partidos = new ArrayList<>();
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
    	partidos.add(nuevo);
    	System.out.println(" + Se creo un nuevo partido de " + nuevo.getDeporte());
    }
    
    public void buscarPartidosPorNivel(Nivel nivel) {
        System.out.println("Buscando partidos con nivel: " + nivel);
        boolean encontrados = false;

        for (int i = 0; i < partidos.size(); i++) {
            Partido partido = partidos.get(i);
            if (partido.getNivel().equals(nivel)) {
                System.out.println("ID Partido: " + i);
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
    	
    	List<Partido> partidosEnEstadoNecesitamosJugadores = new ArrayList<Partido>();
    	for(Partido partido:partidos) {
    		if(partido.getEstado().toString().equals("NecesitamosJugadores")) {
    			partidosEnEstadoNecesitamosJugadores.add(partido);
    		}
    	}
    	
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
    
    public Partido getPartidoPorID(int id) {
    	return partidos.get(id);

    }
    
    public void agregarJugadorAPartido(int idPartido, Usuario jugadorNuevo) {
    	Partido partido = getPartidoPorID(idPartido);
    	partido.agregarJugador(jugadorNuevo);
    	System.out.println("Se agrego jugador "+jugadorNuevo+" al partido: "+partido.getDeporte()+" del nivel: "+partido.getNivel());
    	System.out.println("-----------------------------------");
    	
    }
    
    public void printEstadoPartidoID (int id) {
    	System.out.println(partidos.get(id).getEstado()); 
    }
    
    public Partido getPartidoID(int id) {
    	return partidos.get(id);
    }
    
    // Metodos de estado
    
    public void armarPartido(int id) {
        Partido partido = partidos.get(id);
        if (partido.getEmparejamiento() != null) {
            System.out.println("🔄 Armando partido segun estrategia de emparejamiento...");
            // Usar solo los jugadores agregados al partido
            List<Usuario> posiblesJugadores = partido.getJugadoresParticipan();
            List<Usuario> seleccionados = partido.getEmparejamiento().emparejar(partido, posiblesJugadores);
            partido.setJugadoresParticipan(seleccionados);
            if (partido.getJugadoresParticipan().size() >= partido.getCantidadJugadores()) {
                partido.getEstado().armar(partido);
                System.out.println("✅ Partido armado con éxito con estrategia: " + partido.getEmparejamiento().toString());
            } else {
                System.out.println("⚠️ No se pudo armar el partido. Jugadores seleccionados: " + seleccionados.size() + " --> falta de Jugadores Online");
            }
            System.out.println("-----------------------------------");
        }
    }
    
    public void confirmarPartido(int id, Usuario jugador) {
    	Partido partido = partidos.get(id);
    	EstadoPartido estadoActual = partido.getEstado();
    	if(partido.esParticipante(jugador)) {
	    	estadoActual.confirmar(partido);
	    	estadoActual.getMessage(partido);
	    	
    	}
    	else {
    		System.err.println("El " + jugador.getNombre()+ " que intenta confirmar no es parte del Partido");
		}
    	
    }
    
    public void comenzarPartido(int id, Usuario jugador, boolean overrideHorario) {
        Partido partido = partidos.get(id);
        EstadoPartido estadoActual = partido.getEstado();
        if (partido.esCreador(jugador)) {
            if (overrideHorario && estadoActual instanceof com.tpo.armarPartido.service.estados.Confirmacion) {
                partido.cambiarEstado(new com.tpo.armarPartido.service.estados.EnJuego());
                System.out.println("El partido ha comenzado (override horario).");
            } else {
                estadoActual.comenzar(partido);
                estadoActual.getMessage(partido);
            }
        } else {
            System.err.println("El " + jugador.getNombre() + " que intenta comenzar no es creador del partido");
        }
    }
    
    public void finalizarPartido(int id, Usuario jugador, boolean overrideHorario) {
        Partido partido = partidos.get(id);
        EstadoPartido estadoActual = partido.getEstado();
        if (partido.esCreador(jugador)) {
            if (overrideHorario && estadoActual instanceof com.tpo.armarPartido.service.estados.EnJuego) {
                partido.cambiarEstado(new com.tpo.armarPartido.service.estados.Finalizado());
                System.out.println("El partido ha finalizado (override horario).");
            } else {
                estadoActual.finalizar(partido);
                estadoActual.getMessage(partido);
            }
        } else {
            System.err.println("El " + jugador.getNombre() + " que intenta comenzar no es creador del partido");
        }
    }

    // Overloads for backward compatibility
    public void comenzarPartido(int id, Usuario jugador) {
        comenzarPartido(id, jugador, false);
    }

    public void finalizarPartido(int id, Usuario jugador) {
        finalizarPartido(id, jugador, false);
    }

    public List<PartidoDTO> getPartidosDTO() {
        return partidos.stream().map(DTOMapper::toPartidoDTO).collect(Collectors.toList());
    }

    public PartidoDTO getPartidoDTOPorID(int id) {
        if (id >= 0 && id < partidos.size()) {
            return DTOMapper.toPartidoDTO(partidos.get(id));
        }
        return null;
    }
    
    public void comentarPartido(int id, Usuario jugador, String comentario) {
    	Partido partido = partidos.get(id);
    	EstadoPartido estadoActual = partido.getEstado();
    	if(estadoActual.toString().equalsIgnoreCase("Finalizado")) {
    		if(partido.esParticipante(jugador)) {
    			partido.comentar(jugador, comentario);
    			System.out.println("El jugador: " + jugador.getNombre() + " dejo el siguiente comentario: " + comentario);
    		}
    	}
    	
    }

    @GetMapping
    public List<PartidoDTO> listarPartidos() {
        return getPartidosDTO();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> obtenerPartido(@PathVariable int id) {
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
    public ResponseEntity<Void> eliminarPartido(@PathVariable int id) {
        if (id >= 0 && id < partidos.size()) {
            partidos.remove(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/agregar-jugador")
    public ResponseEntity<Void> agregarJugador(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
        agregarJugadorAPartido(id, jugador);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/armar")
    public ResponseEntity<Void> armar(@PathVariable int id) {
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
        armarPartido(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmar(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
        confirmarPartido(id, jugador);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comenzar")
    public ResponseEntity<Void> comenzar(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO,
                                         @RequestParam(defaultValue = "false") boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
        comenzarPartido(id, jugador, overrideHorario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO,
                                          @RequestParam(defaultValue = "false") boolean overrideHorario) {
        Usuario jugador = DTOMapper.toUsuario(usuarioDTO, "");
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
        finalizarPartido(id, jugador, overrideHorario);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comentar")
    public ResponseEntity<Void> comentar(@PathVariable int id, @RequestBody ComentarioRequest comentarioRequest) {
        Usuario jugador = DTOMapper.toUsuario(comentarioRequest.getUsuario(), "");
        if (id < 0 || id >= partidos.size()) return ResponseEntity.notFound().build();
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
