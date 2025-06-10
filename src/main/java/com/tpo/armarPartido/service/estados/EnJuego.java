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
		Date inicio = partido.getHorario();
		long duracionMinutos = partido.getDuracion();
		long duracionMillis = duracionMinutos * 60L * 1000L;
		Date ahora = new Date();
		Date finEstimado = new Date(inicio.getTime() + duracionMillis);
		if (ahora.after(finEstimado)) {
		    partido.cambiarEstado(new Finalizado());
		 }
	}

}
