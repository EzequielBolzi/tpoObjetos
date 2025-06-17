package com.tpo.armarPartido;

import com.tpo.armarPartido.controller.ControllerPartido;
import com.tpo.armarPartido.controller.ControllerUsuario;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import utils.GenerarPartidos;
import utils.GenerarUsuarios;

public class Main {
    public static void main(String[] args) {
    	
    	// System.out.println("Inicio App ArmarPartido");
    
    	// // Instancio controladores. 
    	// ControllerUsuario userController = ControllerUsuario.getInstancia();
    	// ControllerPartido partidoController = ControllerPartido.getInstancia();
    	
    	// // Genero Usuarios
    	// GenerarUsuarios.generar(userController);    
        
    	// // Genero Partidos
    	// GenerarPartidos.generar(userController, partidoController);

    	// // Instancio usuarios para Usar
        // Usuario Luis = userController.getUsuarioPorNombre("Luis Martinez");
        // Usuario Ana = userController.getUsuarioPorNombre("Ana Gomez");
        // Usuario Juan = userController.getUsuarioPorNombre("Juan Perez");
        
        // //Prueba para agregar deporte y nivel a un usuario.
        // // userController.agregarDeporteNivel("ana@example.com", Deporte.BASQUET, Nivel.PRINCIPIANTE);
        
        // //Prueba de busqueda de partidos por ubicacion de parte de un Usuario. 
        // // Ubicacion ubicacionLuis = Ana.getUbicacion();
        // // int cantidadDePartidosMasCercanos = 2;
        // // partidoController.buscarPartidosPorUbicacion(ubicacionLuis, cantidadDePartidosMasCercanos);
        
        // // Busco los partidos por nivel, por ejemplo avanzado 
        // // partidoController.buscarPartidosPorNivel(Nivel.AVANZADO);
        // // Armo el partido que elegi y se agregan de jugadores a partir de la estrategia definida en el partido. 
        // int idPartidoElegido = 0;
        // partidoController.armarPartido(idPartidoElegido);
        // // Pido las confirmaciones de los jugadores participantes. 
        // partidoController.confirmarPartido(0, Luis);
        // partidoController.confirmarPartido(0, Juan);
        // // Solo el creador puedo comenzar el partido en su horario y finalizarlo una vez que haya pasado terminado su horario segun la duracion.
        // partidoController.comenzarPartido(0, Luis);
        // partidoController.finalizarPartido(0, Luis);
        
        // partidoController.comentarPartido(idPartidoElegido, Juan, "Me encanto el partido!!");
    }
} 