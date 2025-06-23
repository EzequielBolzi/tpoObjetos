package com.tpo.armarPartido.exception;

import java.util.List;

/**
 * Excepción lanzada cuando se intenta crear un partido con datos inválidos
 */
public class PartidoInvalidoException extends RuntimeException {
    private final String codigoError;
    private final int statusCode;
    private final List<String> errores;

    public PartidoInvalidoException(String mensaje) {
        super(mensaje);
        this.codigoError = "PARTIDO_INVALIDO";
        this.statusCode = 400;
        this.errores = null;
    }

    public PartidoInvalidoException(String mensaje, List<String> errores) {
        super(mensaje);
        this.codigoError = "PARTIDO_INVALIDO";
        this.statusCode = 400;
        this.errores = errores;
    }

    public PartidoInvalidoException(String codigoError, String mensaje, int statusCode, List<String> errores) {
        super(mensaje);
        this.codigoError = codigoError;
        this.statusCode = statusCode;
        this.errores = errores;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<String> getErrores() {
        return errores;
    }
} 