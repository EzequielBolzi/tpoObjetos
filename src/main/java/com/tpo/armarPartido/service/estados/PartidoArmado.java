package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.*;

public class PartidoArmado implements EstadoPartido {

    private static final String mensaje = "Ya sos parte del partido %s! Nivel: %s Por favor envia tu confirmacion al partido del jugador:  %s \n -------------------------";

    public PartidoArmado() {}

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }
    
    @Override
	public String toString() {
		return "PartidoArmado";
	}

	@Override
    public void armar(Partido partido) {
        // No aplica a este estado
    }

    @Override
    public void confirmar(Partido partido) {
        int confirmaciones = partido.getConfirmaciones();
        confirmaciones++;
        partido.setConfirmaciones(confirmaciones);
        System.out.println("Un usuario confirmo! Tenemos "+ confirmaciones + " confirmaciones en total. " );
        if (confirmaciones >= partido.getJugadoresParticipan().size()) {
            partido.cambiarEstado(new Confirmacion());            
        }
    }

    @Override
    public void comenzar(Partido partido) {
        // No aplica a este estado
    }

    @Override
    public void finalizar(Partido partido) {
        // No aplica a este estado
    }

    @Override
    public String getMessage(Partido partido) {
        return String.format(mensaje, partido.getDeporte(), partido.getNivel(), partido.getCreadorPartido(partido));
    }

	@Override
	public void comentar(Usuario jugador, String comentario, Partido partido) {
		// No aplica a este estado
	}
}