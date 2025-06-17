package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmparejamientoPorUbicacion implements EstrategiaEmparejamiento {
    @Override
    public String toString() {
        return "Emparejamiento Por Ubicación";
    }

    @Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
        Ubicacion ubicacionCentral = partido.getUbicacion();
        List<Usuario> seleccionados = new ArrayList<>();
        if (jugadores != null && !jugadores.isEmpty()) {
            seleccionados.add(jugadores.get(0)); 
            jugadores.stream()
                .filter(j -> !j.equals(jugadores.get(0)))
                .sorted(Comparator.comparingDouble(j -> ubicacionCentral.distanciaCuadradoA(j.getUbicacion())))
                .forEach(seleccionados::add);
        }
        return seleccionados;
    }
}

