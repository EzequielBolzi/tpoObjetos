package com.tpo.armarPartido.service;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {
    @Override
    public String toString() {
        return "Emparejamiento Por Niveles disponibles: ";
    }

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" -- Emparejamiento por Nivel");

        // Mostrar niveles disponibles
        Nivel[] todosNiveles = Nivel.values();
        for(int i = 0; i < todosNiveles.length; i++) {
            System.out.println(i + ": " + todosNiveles[i]);
        }

        System.out.print("Elige el índice para NIVEL_MIN: ");
        int NIVEL_MIN = scanner.nextInt();
        System.out.print("Elige el índice para NIVEL_MAX: ");
        int NIVEL_MAX = scanner.nextInt();

        // Validar índices
        if (NIVEL_MIN < 0 || NIVEL_MAX >= todosNiveles.length || NIVEL_MIN > NIVEL_MAX) {
            System.out.println("Error: índices inválidos.");
            return new ArrayList<>();
        }

        Deporte deporte = partido.getDeporte();
        List<Usuario> seleccionados = new ArrayList<>();

        // Obtener los que ya están en el partido
        List<Usuario> yaParticipan = partido.getJugadoresParticipan();
        if (yaParticipan != null) {
            seleccionados.addAll(yaParticipan);
        }

        // Calcular cuántos jugadores más necesitamos
        int jugadoresNecesarios = partido.getCantidadJugadores() - seleccionados.size();
        if (jugadoresNecesarios <= 0) {
            return seleccionados.subList(0, partido.getCantidadJugadores());
        }

        // Filtrar jugadores disponibles que no estén ya agregados y que cumplan con el nivel
        List<Usuario> jugadoresDisponibles = jugadores.stream()
                .filter(jugador -> !seleccionados.contains(jugador))
                .filter(jugador -> {
                    Nivel nivelJugador = jugador.getNivelesPorDeporte().get(deporte);
                    return nivelJugador != null &&
                            nivelJugador.ordinal() >= NIVEL_MIN &&
                            nivelJugador.ordinal() <= NIVEL_MAX;
                })
                .limit(jugadoresNecesarios)
                .collect(Collectors.toList());

        // Agregar los jugadores que cumplen con el criterio de nivel
        seleccionados.addAll(jugadoresDisponibles);

        return seleccionados;
    }
}