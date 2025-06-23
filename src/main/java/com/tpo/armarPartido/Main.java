package com.tpo.armarPartido;

import com.tpo.armarPartido.controller.ControllerPartido;
import com.tpo.armarPartido.controller.ControllerUsuario;
import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.PartidoDTO;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import utils.GenerarPartidos;
import utils.GenerarUsuarios;
import io.javalin.Javalin;
import com.tpo.armarPartido.repository.UsuarioRepository;
import com.tpo.armarPartido.repository.PartidoRepository;
import com.tpo.armarPartido.DataInitializer;
import com.tpo.armarPartido.exception.GlobalExceptionHandler;

public class Main {
    public static void main(String[] args) {
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        PartidoRepository partidoRepo = new PartidoRepository();
        DataInitializer.init(usuarioRepo, partidoRepo);
        ControllerUsuario userController = new ControllerUsuario();
        ControllerPartido partidoController = new ControllerPartido();
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8080);

        // Configurar manejador global de excepciones
        GlobalExceptionHandler.configure(app);

        ApiEndpoints.register(app, userController, partidoController);
    }
} 