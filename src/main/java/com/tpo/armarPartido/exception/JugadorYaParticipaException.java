package com.tpo.armarPartido.exception;

/**
 * Excepción lanzada cuando se intenta agregar un jugador que ya participa en el partido
 */
public class JugadorYaParticipaException extends PartidoException {
    
    public JugadorYaParticipaException(String nombreJugador, Long partidoId) {
        super("El jugador " + nombreJugador + " ya participa en el partido " + partidoId, 
              "JUGADOR_YA_PARTICIPA", 400);
    }
    
    public JugadorYaParticipaException(String nombreJugador) {
        super("El jugador " + nombreJugador + " ya participa en este partido", 
              "JUGADOR_YA_PARTICIPA", 400);
    }
} 