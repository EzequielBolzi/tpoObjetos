package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmparejamientoPorUbicacion implements EstrategiaEmparejamiento {
    @Override
    public String toString() {
        return "Emparejamiento Por Ubicación";
    }

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
        Ubicacion ubicacionCentral = partido.getUbicacion();
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

        // Filtrar jugadores disponibles que no estén ya agregados
        List<Usuario> jugadoresDisponibles = jugadores.stream()
                .filter(jugador -> !seleccionados.contains(jugador))
                .sorted(Comparator.comparingDouble(j -> ubicacionCentral.distanciaCuadradoA(j.getUbicacion())))
                .limit(jugadoresNecesarios)
                .collect(Collectors.toList());

        // Agregar los más cercanos
        seleccionados.addAll(jugadoresDisponibles);

        return seleccionados;
    }
}


