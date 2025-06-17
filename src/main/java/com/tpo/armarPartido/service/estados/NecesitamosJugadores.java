package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;


public class NecesitamosJugadores implements EstadoPartido {

    private static final String mensaje = "Faltan %d jugadores de %d para el partido.";

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }

    @Override
	public String toString() {
		return "NecesitamosJugadores";
	}

	@Override
    public void armar(Partido partido) {
        if (partido.getJugadoresParticipan().size() >= partido.getCantidadJugadores()) {
            partido.cambiarEstado(new PartidoArmado());
            System.out.println("El partido tiene todos los jugadores! Ya esta armado y listo para confirmar.");
        }
        else {System.out.println("El partido no tiene todos los jugadores para armar.");}

    }

    @Override
    public void confirmar(Partido partido) {
        // No aplica a este estado
    }

    @Override
    public void comenzar(Partido partido) {
        // No aplica a este estado
    }

    @Override
    public void finalizar(Partido partido) {
        // No aplica a este estado
    }

    public String getMessage(Partido partido) {
        return mensaje;
    }

	@Override
	public void comentar(Usuario jugador, String comentario, Partido partido) {
		// No aplica a este estado
		
	}



}