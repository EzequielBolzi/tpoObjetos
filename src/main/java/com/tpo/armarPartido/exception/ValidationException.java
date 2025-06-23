package com.tpo.armarPartido.exception;

import java.util.List;
import java.util.ArrayList;

/**
 * Excepción lanzada cuando hay errores de validación
 */
public class ValidationException extends PartidoException {
    private final List<String> errores;
    
    public ValidationException(String mensaje) {
        super(mensaje, "VALIDATION_ERROR", 400);
        this.errores = new ArrayList<>();
        this.errores.add(mensaje);
    }
    
    public ValidationException(String mensaje, List<String> errores) {
        super(mensaje, "VALIDATION_ERROR", 400);
        this.errores = errores != null ? errores : new ArrayList<>();
    }
    
    public ValidationException(List<String> errores) {
        super("Errores de validación encontrados", "VALIDATION_ERROR", 400);
        this.errores = errores != null ? errores : new ArrayList<>();
    }
    
    public List<String> getErrores() {
        return errores;
    }
    
    public void agregarError(String error) {
        this.errores.add(error);
    }
} 