package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;
import java.util.List;
import com.tpo.armarPartido.model.Comentario;

public class Finalizado implements EstadoPartido {
	
	private static final String mensaje = "El partido ya finalizo!";
	private List<Comentario> comentarios;

	@Override
	public void cancelar(Partido partido) {
		// No aplica.
		
	}

	@Override
	public void armar(Partido partido) {
		// No aplica.
		
	}

	@Override
	public void confirmar(Partido partido) {
		// No aplica.
		
	}

	@Override
	public void comenzar(Partido partido) {
		// No aplica.
		
	}

	@Override
	public void finalizar(Partido partido) {
		// No aplica.
		
	}
	
	public void agregarComentario(Comentario comentario) {
		comentarios.add(comentario);
	}

}
