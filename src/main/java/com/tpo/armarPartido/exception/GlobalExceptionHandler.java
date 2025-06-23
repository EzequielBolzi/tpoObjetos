package com.tpo.armarPartido.exception;

import com.tpo.armarPartido.dto.ErrorResponse;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.HttpStatus;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Manejador global de excepciones para la aplicación
 */
public class GlobalExceptionHandler {
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    public static void configure(Javalin app) {
        // Manejador para excepciones personalizadas de la aplicación
        app.exception(PartidoException.class, (e, ctx) -> {
            logger.log(Level.WARNING, "Excepción de aplicación: " + e.getMessage(), e);
            handlePartidoException(e, ctx);
        });

        // Manejador para excepciones de validación
        app.exception(ValidationException.class, (e, ctx) -> {
            logger.log(Level.WARNING, "Error de validación: " + e.getMessage(), e);
            handleValidationException(e, ctx);
        });

        // Manejador para excepciones de usuario no encontrado
        app.exception(UsuarioNotFoundException.class, (e, ctx) -> {
            logger.log(Level.INFO, "Usuario no encontrado: " + e.getMessage());
            handleUsuarioNotFoundException(e, ctx);
        });

        // Manejador para excepciones de partido no encontrado
        app.exception(PartidoNotFoundException.class, (e, ctx) -> {
            logger.log(Level.INFO, "Partido no encontrado: " + e.getMessage());
            handlePartidoNotFoundException(e, ctx);
        });

        // Manejador para excepciones de autorización
        app.exception(UnauthorizedException.class, (e, ctx) -> {
            logger.log(Level.WARNING, "Error de autorización: " + e.getMessage());
            handleUnauthorizedException(e, ctx);
        });

        // Manejador para excepciones de jugador ya participa
        app.exception(JugadorYaParticipaException.class, (e, ctx) -> {
            logger.log(Level.INFO, "Jugador ya participa: " + e.getMessage());
            handleJugadorYaParticipaException(e, ctx);
        });

        // Manejador para excepciones de partido inválido
        app.exception(PartidoInvalidoException.class, (e, ctx) -> {
            logger.log(Level.WARNING, "Partido inválido: " + e.getMessage());
            handlePartidoInvalidoException(e, ctx);
        });

        // Manejador para excepciones de IllegalArgumentException
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            logger.log(Level.WARNING, "Argumento ilegal: " + e.getMessage(), e);
            handleIllegalArgumentException(e, ctx);
        });

        // Manejador para excepciones de RuntimeException
        app.exception(RuntimeException.class, (e, ctx) -> {
            logger.log(Level.SEVERE, "Error de runtime: " + e.getMessage(), e);
            handleRuntimeException(e, ctx);
        });

        // Manejador para excepciones generales
        app.exception(Exception.class, (e, ctx) -> {
            logger.log(Level.SEVERE, "Error inesperado: " + e.getMessage(), e);
            handleGenericException(e, ctx);
        });
    }

    private static void handlePartidoException(PartidoException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handleValidationException(ValidationException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path(),
            e.getErrores()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handleUsuarioNotFoundException(UsuarioNotFoundException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handlePartidoNotFoundException(PartidoNotFoundException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handleUnauthorizedException(UnauthorizedException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handleJugadorYaParticipaException(JugadorYaParticipaException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handlePartidoInvalidoException(PartidoInvalidoException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            e.getCodigoError(),
            e.getMessage(),
            e.getStatusCode(),
            ctx.path()
        );
        ctx.status(e.getStatusCode()).json(errorResponse);
    }

    private static void handleIllegalArgumentException(IllegalArgumentException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INVALID_ARGUMENT",
            e.getMessage(),
            HttpStatus.BAD_REQUEST.getCode(),
            ctx.path()
        );
        ctx.status(HttpStatus.BAD_REQUEST).json(errorResponse);
    }

    private static void handleRuntimeException(RuntimeException e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            "RUNTIME_ERROR",
            "Error interno del servidor: " + e.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
            ctx.path()
        );
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
    }

    private static void handleGenericException(Exception e, Context ctx) {
        ErrorResponse errorResponse = new ErrorResponse(
            "INTERNAL_ERROR",
            "Error interno del servidor",
            HttpStatus.INTERNAL_SERVER_ERROR.getCode(),
            ctx.path()
        );
        ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).json(errorResponse);
    }
} 