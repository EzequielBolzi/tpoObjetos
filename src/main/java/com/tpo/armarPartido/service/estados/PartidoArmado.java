package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.dto.UsuarioDTO;
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
        // Debemos validar antes que el usuario este en este partido. 
        confirmaciones++;
        if (confirmaciones >= partido.getJugadoresParticipan().size()) {
            partido.cambiarEstado(new Confirmacion());
        }
    }

    @Override
    public void comenzar(Partido partido) {
        // No aplica. 
    }

    @Override
    public void finalizar(Partido partido) {
        // No aplica.
    }

    public String getMensaje() {
        return String.format(mensaje, confirmaciones);
    }
}
