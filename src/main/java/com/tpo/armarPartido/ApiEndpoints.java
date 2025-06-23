package com.tpo.armarPartido;

// Endpoints REST para la API principal de la aplicación. Define rutas para usuarios y partidos.
import com.tpo.armarPartido.controller.ControllerPartido;
import com.tpo.armarPartido.controller.ControllerUsuario;
import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.PartidoDTO;
import com.tpo.armarPartido.enums.Nivel;
import io.javalin.Javalin;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

public class ApiEndpoints {
    public static void register(Javalin app, ControllerUsuario userController, ControllerPartido partidoController) {
        app.get("/api/health", ctx -> ctx.result("OK"));

        // --- Endpoints de Usuarios ---
        // Obtener todos los usuarios o filtrar por nivel si se pasa el parámetro 'nivel'
        app.get("/api/usuarios", ctx -> {
            String nivelParam = ctx.queryParam("nivel");
            if (nivelParam != null) {
                Nivel nivel;
                try {
                    nivel = Nivel.valueOf(nivelParam.toUpperCase());
                } catch (Exception e) {
                    ctx.status(400).result("Nivel inválido");
                    return;
                }
                List<UsuarioDTO> usuarios = userController.getAllUsuarios().stream()
                    .filter(u -> u.getNivelesPorDeporte() != null && u.getNivelesPorDeporte().values().contains(nivel))
                    .collect(Collectors.toList());
                ctx.json(usuarios);
            } else {
                ctx.json(userController.getAllUsuarios());
            }
        });

        // Obtener un usuario por nombre
        app.get("/api/usuarios/{nombre}", ctx -> {
            String nombre = ctx.pathParam("nombre");
            UsuarioDTO usuario = userController.getUsuarioByNombre(nombre);
            if (usuario != null) {
                ctx.json(usuario);
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
        });

        // Crear un nuevo usuario
        app.post("/api/usuarios", ctx -> {
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            String contrasena = ctx.queryParam("contrasena");
            if (contrasena == null) contrasena = "";
            userController.createUsuario(usuarioDTO, contrasena);
            ctx.status(201);
        });

        // Eliminar un usuario por correo
        app.delete("/api/usuarios/{correo}", ctx -> {
            String correo = ctx.pathParam("correo");
            userController.deleteUsuario(correo);
            ctx.status(204);
        });

        // --- Endpoints de Partidos ---
        // Buscar partidos con filtros opcionales: nivel, latitud, longitud y cantidad
        app.get("/api/partidos", ctx -> {
            String nivelParam = ctx.queryParam("nivel");
            String latParam = ctx.queryParam("lat");
            String lngParam = ctx.queryParam("lng");
            String cantidadParam = ctx.queryParam("cantidad");
            Nivel nivel = null;
            Double lat = null, lng = null;
            Integer cantidad = null;
            if (nivelParam != null) {
                try {
                    nivel = Nivel.valueOf(nivelParam.toUpperCase());
                } catch (Exception e) {
                    ctx.status(400).result("Nivel inválido");
                    return;
                }
            }
            if (latParam != null && lngParam != null) {
                try {
                    lat = Double.parseDouble(latParam);
                    lng = Double.parseDouble(lngParam);
                } catch (Exception e) {
                    ctx.status(400).result("Lat/Lng inválidos");
                    return;
                }
            }
            if (cantidadParam != null) {
                try {
                    cantidad = Integer.parseInt(cantidadParam);
                } catch (Exception e) {
                    ctx.status(400).result("Cantidad inválida");
                    return;
                }
            }
            List<PartidoDTO> partidos = partidoController.buscarPartidos(nivel, lat, lng, cantidad);
            ctx.json(partidos);
        });

        // Obtener un partido por ID
        app.get("/api/partidos/{id}", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            PartidoDTO partido = partidoController.getPartidoDTOPorID(id);
            if (partido != null) {
                ctx.json(partido);
            } else {
                ctx.status(404).result("Partido no encontrado");
            }
        });

        // Crear un nuevo partido
        app.post("/api/partidos", ctx -> {
            PartidoDTO partidoDTO = ctx.bodyAsClass(PartidoDTO.class);
            String emparejamiento = ctx.queryParam("emparejamiento");
            if (emparejamiento == null) emparejamiento = "";
            partidoController.crearPartido(partidoDTO, emparejamiento);
            ctx.status(201);
        });

        // Eliminar un partido por ID
        app.delete("/api/partidos/{id}", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            partidoController.eliminarPartido(id);
            ctx.status(204);
        });

        // Agregar un jugador a un partido existente
        app.post("/api/partidos/{id}/agregar-jugador", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            partidoController.agregarJugador(id, usuarioDTO);
            ctx.status(200);
        });

        // Armar un partido (cambiar de estado si hay suficientes jugadores)
        app.post("/api/partidos/{id}/armar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            partidoController.armar(id);
            ctx.status(200);
        });

        // Confirmar participación de un usuario en un partido
        app.post("/api/partidos/{id}/confirmar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            partidoController.confirmar(id, usuarioDTO);
            ctx.status(200);
        });

        // Comenzar un partido (solo el creador puede hacerlo)
        app.post("/api/partidos/{id}/comenzar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            String overrideParam = ctx.queryParam("overrideHorario");
            boolean overrideHorario = overrideParam != null ? Boolean.parseBoolean(overrideParam) : false;
            partidoController.comenzar(id, usuarioDTO, overrideHorario);
            ctx.status(200);
        });

        // Finalizar un partido (solo el creador puede hacerlo)
        app.post("/api/partidos/{id}/finalizar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            String overrideParam = ctx.queryParam("overrideHorario");
            boolean overrideHorario = overrideParam != null ? Boolean.parseBoolean(overrideParam) : false;
            partidoController.finalizar(id, usuarioDTO, overrideHorario);
            ctx.status(200);
        });

        // Cancelar un partido (solo el creador puede hacerlo)
        app.post("/api/partidos/{id}/cancelar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            UsuarioDTO usuarioDTO = ctx.bodyAsClass(UsuarioDTO.class);
            try {
                partidoController.cancelar(id, usuarioDTO);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Partido cancelado exitosamente");
                ctx.status(200).json(response);
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", e.getMessage());
                ctx.status(400).json(errorResponse);
            }
        });

        // Comentar un partido (solo participantes y si el partido está finalizado)
        app.post("/api/partidos/{id}/comentar", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            ControllerPartido.ComentarioRequest comentarioRequest = ctx.bodyAsClass(ControllerPartido.ComentarioRequest.class);
            partidoController.comentar(id, comentarioRequest);
            ctx.status(200);
        });
    }
} 