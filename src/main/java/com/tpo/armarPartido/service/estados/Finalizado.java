package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;
import java.util.List;
import com.tpo.armarPartido.model.Comentario;

public class Finalizado implements EstadoPartido {
	
	private static final String mensaje = "El partido ya finalizo!";
	private List<Comentario> Comentarios;

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
		// TODO Auto-generated method stub
		
	}

}
