package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import com.tpo.armarPartido.model.Comentario;

public class Finalizado implements EstadoPartido {

    private static final String mensaje = "El partido ya finalizo! \n -------------------------";
    private List<Comentario> comentarios = new ArrayList<Comentario>();

    public Finalizado() {}

    public Finalizado(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
	public String toString() {
		return "Finalizado";
	}

	@Override
    public void cancelar(Partido partido) {
        // No aplica a este estado

    }

    @Override
    public void armar(Partido partido) {
        // No aplica a este estado

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

    @Override
    public String getMessage(Partido partido) {
        return mensaje;

    }


	@Override
	public void comentar(Usuario jugador, String comentario, Partido partido) {
		Comentario comentarioNuevo = new Comentario(jugador, comentario, partido);
		comentarios.add(comentarioNuevo);
	}



}