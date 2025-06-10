package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;



public class NecesitamosJugadores implements EstadoPartido {

    private static final String mensaje = "Faltan %d jugadores de %d para el partido.";

    @Override
    public void cancelar(Partido partido) {
        partido.cambiarEstado(new Cancelado());
    }

    @Override
    public void armar(Partido partido) {
        if (partido.getJugadoresParticipan().size() == partido.getCantidadJugadores()) {
            partido.cambiarEstado(new PartidoArmado());
            System.out.println("El partido tiene todos los jugadores! Ya esta armado y listo para confirmar.");
        }
        else {System.out.println("El partido no tiene todos los jugadores para armar.");}
        
    }

    @Override
    public void confirmar(Partido partido) {
        // No se permite confirmar en este estado
    }

    @Override
    public void comenzar(Partido partido) {
        // No se permite comenzar en este estado
    }

    @Override
    public void finalizar(Partido partido) {
        // No se permite finalizar en este estado
    }
    
    public String getMensaje(Partido partido) {
        int faltantes = partido.getCantidadJugadores() - partido.getJugadoresParticipan().size();
        return String.format(mensaje, faltantes, partido.getCantidadJugadores());
    }
    

}
