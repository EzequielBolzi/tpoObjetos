package com.tpo.armarPartido.service.estados;

import java.util.Date;

import com.tpo.armarPartido.model.Partido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class EnJuego implements EstadoPartido {
    private static final String mensaje = "El partido ya comenzo!";

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

    @Override
    public String getMessage() {
        return mensaje;

    }

}