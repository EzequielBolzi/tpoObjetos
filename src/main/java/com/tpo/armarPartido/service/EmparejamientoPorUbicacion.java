package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorUbicacion implements EstrategiaEmparejamiento {

    private static final int DISTANCIA_MAXIMA = 100;

    @Override
    public String toString() {
        return "Emparejamiento Por Ubicacion";
    }

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
        Ubicacion ubicacionPartido = partido.getUbicacion();
        List<Usuario> jugadoresSeleccionados = new ArrayList<>();

        // Siempre agrego al creador del partido
        int jugadorCreador = 0;
        jugadoresSeleccionados.add(partido.getJugadoresParticipan().get(jugadorCreador));

        // Filtrar y ordenar candidatos por cercanía
        List<Usuario> candidatos = jugadores.stream()
            .filter(j -> !jugadoresSeleccionados.contains(j))
            .filter(j -> j.getUbicacion() != null)
            .sorted(Comparator.comparingDouble(j -> calcularDistancia(ubicacionPartido, j.getUbicacion())))
            .collect(Collectors.toList());

        for (Usuario jugador : candidatos) {
            double distancia = calcularDistancia(ubicacionPartido, jugador.getUbicacion());
            if (jugadoresSeleccionados.size() >= partido.getCantidadJugadores()) {
                break;
            }
            System.out.printf("Distancia entre partido (%s) y jugador %s (%s): %.2f\n",
                ubicacionPartido.toString(),
                jugador.getNombre(),
                jugador.getUbicacion().toString(),
                distancia
            );

            if (distancia <= DISTANCIA_MAXIMA) {
                jugadoresSeleccionados.add(jugador);
                System.out.println("✅ Jugador agregado por cercanía: " + jugador.getNombre());
            } else {
                System.out.println("❌ Jugador muy lejos: " + jugador.getNombre());
            }
        }

        return jugadoresSeleccionados;
    }

    private double calcularDistancia(Ubicacion ubi1, Ubicacion ubi2) {
        double x = ubi1.getLatitud() - ubi2.getLatitud();
        double y = ubi1.getLongitud() - ubi2.getLongitud();
        return Math.sqrt(x * x + y * y);
    }
}


