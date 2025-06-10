package com.tpo.armarPartido.service.estados;

import java.util.Date;

import com.tpo.armarPartido.model.Partido;

public class EnJuego implements EstadoPartido {
	private static final String mensaje = "El partido ya comenzo!";

	@Override
	public void cancelar(Partido partido) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void armar(Partido partido) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmar(Partido partido) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void comenzar(Partido partido) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finalizar(Partido partido) {
	    Date ahora = new Date();
	    long inicioMillis = partido.getHorario().getTime();
	    long duracionMillis = partido.getDuracion() * 60L * 1000L;
	    long finEstimadoMillis = inicioMillis + duracionMillis;

	    if (ahora.getTime() >= finEstimadoMillis) {
	        partido.cambiarEstado(new Finalizado());
	        System.out.println("El partido ha finalizado.");
	    } else {
	        System.out.println("El partido aún no ha finalizado. Final estimado: " + new Date(finEstimadoMillis));
	        }
	}

}
