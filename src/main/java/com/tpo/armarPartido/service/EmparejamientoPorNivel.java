package com.tpo.armarPartido.service;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {

	
    @Override
	public String toString() {
		return "Emparejamiento Por Niveles disponibles: ";
	}

	@Override
    public List<Usuario> emparejar(Partido partido, List<Usuario> jugadores) {
		// El usuario por consola debe cargar el rango de niveles posibles.
		
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
        
        // Siempre agrego al creador del partido.
        
        Nivel nivelRequerido = partido.getNivel();
        Deporte deporte = partido.getDeporte();
        List<Usuario> jugadoresSeleccionados = new ArrayList<>();
        List<Usuario> jugadoresPreAnotados = partido.getJugadoresParticipan();
        for(Usuario jugador : jugadoresPreAnotados) {
        	jugadoresSeleccionados.add(jugador);
        }
        
        
        if (NIVEL_MIN < 0 || NIVEL_MAX >= todosNiveles.length || NIVEL_MIN > NIVEL_MAX) {
            System.out.println("Error: índices inválidos.");
            return jugadoresSeleccionados; //Si los indices estan mal, devuelvo lista con solo el jugador creador
        }
        
        //Agrego a los jugadores por emparejamiento de nivel
        
        for(int i=NIVEL_MAX; i >= NIVEL_MIN; i--) {
            Nivel nivelActual = todosNiveles[i];
            System.out.println("Verificando jugadores de nivel -> " + nivelActual);
        	for(Usuario jugador : jugadores) {
                if (jugadoresSeleccionados.contains(jugador)) {
                    continue; 
                }
                Nivel nivelJugador = jugador.getNivelesPorDeporte().get(deporte);
                System.out.println("  Jugador " + jugador.getNombre() + " tiene nivel " + nivelJugador);
                if (nivelJugador != null && nivelJugador == Nivel.values()[i]) {
                    jugadoresSeleccionados.add(jugador);
                    System.out.println("Emparejando Jugadores por Nivel " + jugador.getNombre() + " agregado.");
                    if (jugadoresSeleccionados.size() >= partido.getCantidadJugadores()) {
                        break;
                    }
                }
        	}
        	
        }
        return jugadoresSeleccionados;
    }


}
