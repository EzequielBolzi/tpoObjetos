package com.tpo.armarPartido.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DTO para respuestas de error estandarizadas
 */
public class ErrorResponse {
    private String codigoError;
    private String mensaje;
    private int statusCode;
    private String timestamp;
    private String path;
    private List<String> errores;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public ErrorResponse(String codigoError, String mensaje, int statusCode, String path) {
        this();
        this.codigoError = codigoError;
        this.mensaje = mensaje;
        this.statusCode = statusCode;
        this.path = path;
    }

    public ErrorResponse(String codigoError, String mensaje, int statusCode, String path, List<String> errores) {
        this(codigoError, mensaje, statusCode, path);
        this.errores = errores;
    }

    // Getters y Setters
    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<String> getErrores() { return errores; }
    public void setErrores(List<String> errores) { this.errores = errores; }
} 