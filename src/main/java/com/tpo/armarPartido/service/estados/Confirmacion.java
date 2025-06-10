package com.tpo.armarPartido.service.estados;


import java.util.Date;


import com.tpo.armarPartido.model.Partido;

public class Confirmacion implements EstadoPartido {
	private static final String mensaje = "El partido esta listo para comenzar.";
	
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
		// No aplica. Los jugadores ya estan todos confirmados
		
	}

	@Override
	public void comenzar(Partido partido) {
	    Date ahora = new Date();
	    long horarioPartido = partido.getHorario().getTime();
	    long ahoraMillis = ahora.getTime();
	    long diezMinutos = 600000;

	    if (ahoraMillis >= (horarioPartido - diezMinutos) && ahoraMillis <= (horarioPartido + diezMinutos)) {
	        partido.cambiarEstado(new EnJuego());
	        System.out.println("El partido ha comenzado.");
	    } else {
	        System.out.println("No es el horario para comenzar el partido. Horario programado: " + partido.getHorario());
	    }
	}

	@Override
	public void finalizar(Partido partido) {
		// no aplica
		
	}
	
}
