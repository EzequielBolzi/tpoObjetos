package com.tpo.armarPartido.service.estados;


import java.util.Date;


import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;

public class Confirmacion implements EstadoPartido {
    
	private static final String mensaje = "El partido esta listo para comenzar. \n -------------------------";

    public Confirmacion() {}

    @Override
    public String toString() {
		return "Confirmacion";
	}

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(new Cancelado());

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
        Date ahora = new Date();
        long horarioPartido = partido.getHorario().getTime();
        long ahoraMillis = ahora.getTime();
        long sesentaMinutos = 2 * 60 * 60 * 1000; // A la hora de probar ver el horario del partido 

        if (ahoraMillis >= (horarioPartido - sesentaMinutos) && ahoraMillis <= (horarioPartido + sesentaMinutos)) {
            partido.cambiarEstado(new EnJuego());
            System.out.println("El partido ha comenzado.");
        } else {
            System.out.println("No es el horario para comenzar el partido. Horario programado: " + partido.getHorario());
        }
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
		    // No aplica a este estado
	}

}