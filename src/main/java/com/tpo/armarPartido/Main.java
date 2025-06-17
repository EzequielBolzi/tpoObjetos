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

public class Main {
    public static void main(String[] args) {
        ControllerUsuario userController = new ControllerUsuario();
        ControllerPartido partidoController = new ControllerPartido();
        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8080);

        ApiEndpoints.register(app, userController, partidoController);
    }
} 