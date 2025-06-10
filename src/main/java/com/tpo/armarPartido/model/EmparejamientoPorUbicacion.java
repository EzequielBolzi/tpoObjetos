package com.tpo.armarPartido.model;

import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
import com.tpo.armarPartido.dto.UsuarioDTO;
import org.springframework.stereotype.Component;
import com.tpo.armarPartido.dto.UsuarioDTO;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmparejamientoPorUbicacion implements EstrategiaEmparejamiento {

    private static final double RADIO_MAXIMO_KM = 15.0;
    private static final int TIERRA_RADIO_KM = 6378;

    @Override

    	public List<UsuarioDTO> emparejar(Partido partido, List<UsuarioDTO> jugadores) {
    	Ubicacion ubicacionPartido = partido.getUbicacion();


         return jugadores.stream()
              .filter(jugador -> {
                    Ubicacion ubicacionJugador = null;
                   if (ubicacionJugador == null) return false;

                    double distancia = calcularDistancia(ubicacionPartido, ubicacionJugador);
                    return distancia <= RADIO_MAXIMO_KM;
                })
                .sorted(Comparator.comparingDouble(jugador ->
                       calcularDistancia(ubicacionPartido, jugador.getUbicacion())))
                .limit(partido.getCantidadJugadores())
                .collect(Collectors.toList());
    }

    @Override
    public String getNombreEstrategia() {
        return "Por Ubicación Cercana";
    }

    private double calcularDistancia(Ubicacion origen, Ubicacion destino) {
        // Fórmula de Haversine para calcular distancias reales en base a la curvatura de la tierra

        // Convertir coordenadas de grados a radianes
        double lat1Rad = Math.toRadians(origen.getLatitud());
        double lat2Rad = Math.toRadians(destino.getLatitud());

        // Calcular las diferencias entre coordenadas en radianes
        double deltaLatRad = Math.toRadians(destino.getLatitud() - origen.getLatitud());
        double deltaLonRad = Math.toRadians(destino.getLongitud() - origen.getLongitud());

        // Aplicar la fórmula de Haversine
        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLonRad / 2) * Math.sin(deltaLonRad / 2);

        // Calcular el ángulo central entre los dos puntos
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Multiplicar por el radio de la Tierra para obtener la distancia en kilómetros
        return TIERRA_RADIO_KM * c;
    }
}