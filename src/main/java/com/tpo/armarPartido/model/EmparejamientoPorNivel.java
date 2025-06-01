package com.tpo.armarPartido.model;

import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
import com.tpo.armarPartido.dto.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {

    @Override
    public List<UsuarioDTO> emparejar(Partido partido, List<UsuarioDTO> jugadores) {
        Nivel nivelRequerido = partido.getNivel();

        List<UsuarioDTO> jugadoresCoincidentes = jugadores.stream()
                .filter(jugador -> {
                    Nivel nivelJugador = jugador.getNivel();
                    return nivelJugador != null && nivelCompatible(nivelJugador, nivelRequerido);
                })
                .sorted(Comparator.comparingInt(j -> diferenciaNivel(j.getNivel(), nivelRequerido)))
                .limit(partido.getCantidadJugadores())
                .collect(Collectors.toList());

        return jugadoresCoincidentes;
    }

    private boolean nivelCompatible(Nivel jugador, Nivel requerido) {
        return jugador == requerido;
    }
    private int diferenciaNivel(Nivel nivelJugador, Nivel nivelRequerido) {
        return Math.abs(nivelJugador.ordinal() - nivelRequerido.ordinal());
    }

    @Override
    public String getNombreEstrategia() {
        return "Por Nivel de Habilidad";
    }
}