package com.tpo.armarPartido.exception;

/**
 * Excepción lanzada cuando no se encuentra un partido
 */
public class PartidoNotFoundException extends PartidoException {
    
    public PartidoNotFoundException(Long id) {
        super("Partido no encontrado con ID: " + id, "PARTIDO_NOT_FOUND", 404);
    }
    
    public PartidoNotFoundException(String mensaje, Long id) {
        super(mensaje + ": " + id, "PARTIDO_NOT_FOUND", 404);
    }
} 