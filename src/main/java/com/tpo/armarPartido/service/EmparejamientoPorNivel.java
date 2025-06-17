package com.tpo.armarPartido.service;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {
    @Override
    public String toString() {
        return "Emparejamiento Por Niveles disponibles: ";
    }

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.println(" -- Emparejamiento por Nivel");
        Nivel[] todosNiveles = Nivel.values();
        for(int i = 0; i<todosNiveles.length; i++) {
            System.out.println(i + ": " + todosNiveles[i]);
        }
        System.out.print("Elige el índice para NIVEL_MIN: ");
        int NIVEL_MIN = scanner.nextInt();
        System.out.print("Elige el índice para NIVEL_MAX: ");
        int NIVEL_MAX = scanner.nextInt();
        
        Nivel nivelRequerido = partido.getNivel();
        Deporte deporte = partido.getDeporte();
        List<Usuario> jugadoresSeleccionados = new ArrayList<>();
        int jugadorCreador = 0;
        jugadoresSeleccionados.add(partido.getJugadoresParticipan().get(jugadorCreador));
        if (NIVEL_MIN < 0 || NIVEL_MAX >= todosNiveles.length || NIVEL_MIN > NIVEL_MAX) {
            System.out.println("Error: índices inválidos.");
            return jugadoresSeleccionados; 
        }
        for (Usuario jugador : jugadores) {
            if (!jugador.equals(partido.getJugadoresParticipan().get(jugadorCreador))) {
                Nivel nivelJugador = jugador.getNivelesPorDeporte().get(deporte);
                if (nivelJugador != null && nivelJugador.ordinal() >= NIVEL_MIN && nivelJugador.ordinal() <= NIVEL_MAX) {
                    jugadoresSeleccionados.add(jugador);
                }
            }
        }
        return jugadoresSeleccionados;
    }
}
