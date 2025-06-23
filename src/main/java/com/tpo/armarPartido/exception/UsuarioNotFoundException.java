package com.tpo.armarPartido.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario
 */
public class UsuarioNotFoundException extends PartidoException {
    
    public UsuarioNotFoundException(String correo) {
        super("Usuario no encontrado con correo: " + correo, "USUARIO_NOT_FOUND", 404);
    }
    
    public UsuarioNotFoundException(Long id) {
        super("Usuario no encontrado con ID: " + id, "USUARIO_NOT_FOUND", 404);
    }
    
    public UsuarioNotFoundException(String mensaje, String correo) {
        super(mensaje + ": " + correo, "USUARIO_NOT_FOUND", 404);
    }
} 