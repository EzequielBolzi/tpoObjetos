package com.tpo.armarPartido.exception;

/**
 * Excepción lanzada cuando un usuario no tiene permisos para realizar una acción
 */
public class UnauthorizedException extends PartidoException {
    
    public UnauthorizedException(String mensaje) {
        super(mensaje, "UNAUTHORIZED", 403);
    }
    
    public UnauthorizedException(String accion, String usuario, String recurso) {
        super("El usuario " + usuario + " no tiene permisos para " + accion + " en " + recurso, "UNAUTHORIZED", 403);
    }
    
    public UnauthorizedException(String accion, String usuario) {
        super("El usuario " + usuario + " no tiene permisos para " + accion, "UNAUTHORIZED", 403);
    }
} 