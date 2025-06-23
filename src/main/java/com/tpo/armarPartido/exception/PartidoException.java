package com.tpo.armarPartido.exception;

/**
 * Excepción base para errores relacionados con partidos
 */
public class PartidoException extends RuntimeException {
    private final String codigoError;
    private final int statusCode;

    public PartidoException(String mensaje) {
        super(mensaje);
        this.codigoError = "PARTIDO_ERROR";
        this.statusCode = 400;
    }

    public PartidoException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
        this.statusCode = 400;
    }

    public PartidoException(String mensaje, String codigoError, int statusCode) {
        super(mensaje);
        this.codigoError = codigoError;
        this.statusCode = statusCode;
    }

    public PartidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = "PARTIDO_ERROR";
        this.statusCode = 400;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public int getStatusCode() {
        return statusCode;
    }
} 