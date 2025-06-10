package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.*;

public class PartidoArmado implements EstadoPartido {

    private int confirmaciones = 0;
    private static final String mensaje = "Ya se confirmaron %d jugadores";

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }

    @Override
    public void armar(Partido partido) {
        // No aplica. El partido ya está armado.
    }

    @Override
    public void confirmar(Partido partido) {
        if (partido.getJugadoresParticipan().contains(Usuario)) {
        	confirmaciones++;
        }
    	
        if (confirmaciones >= partido.getJugadoresParticipan().size()) {
            partido.cambiarEstado(new Confirmacion());
        }
    }

    @Override
    public void comenzar(Partido partido) {
        // No aplica. Solo se puede comenzar desde el estado Confirmacion.
    }

    @Override
    public void finalizar(Partido partido) {
        // No aplica. Solo se puede finalizar desde EnJuego.
    }

    public String getMensaje() {
        return String.format(mensaje, confirmaciones);
    }
}
