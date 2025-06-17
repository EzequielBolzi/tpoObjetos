package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
import com.tpo.armarPartido.service.estados.EstadoPartido;

public class utilsPartido {
	
	public static void printPartidos(List<Partido> listaPartidos) {
        if (listaPartidos == null || listaPartidos.isEmpty()) {
            System.out.println("No hay partidos para mostrar.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("----- Lista de partidos (" + listaPartidos.size() + ") -----");
        int idx = 1;
        for (Partido p : listaPartidos) {
            System.out.println("Partido #" + idx + ":");
            Deporte deporte = p.getDeporte();
            System.out.println("  Deporte: " + (deporte != null ? deporte : "N/A"));
            System.out.println("  CantidadJugadores esperada: " + p.getCantidadJugadores());
            System.out.println("  Duración: " + p.getDuracion() + " minutos");
            Ubicacion ub = p.getUbicacion();
            System.out.println("  Ubicación: " + (ub != null ? ub : "N/A"));
            Date horario = p.getHorario();
            if (horario != null) {
                String fechaStr = sdf.format(horario);
                System.out.println("  Horario: " + fechaStr);
            } else {
                System.out.println("  Horario: N/A");
            }

            // Estado
            EstadoPartido estado = p.getEstado();
            System.out.println("  Estado: " + (estado != null ? estado : "N/A"));

            // Estrategia de emparejamiento
            EstrategiaEmparejamiento estrategia = p.getEmparejamiento();
            if (estrategia != null) {
                // Si EstrategiaEmparejamiento tiene toString descriptivo, úsalo; sino mostramos su clase
                String desc = estrategia.toString();
                if (desc == null || desc.isEmpty() || desc.contains("@")) {
                    desc = estrategia.getClass().getSimpleName();
                }
                System.out.println("  EstrategiaEmparejamiento: " + desc);
            } else {
                System.out.println("  EstrategiaEmparejamiento: N/A");
            }

            // JugadoresParticipan
            List<Usuario> jugadores = p.getJugadoresParticipan();
            if (jugadores != null) {
                System.out.println("  JugadoresParticipan (cantidad): " + jugadores.size());
                if (!jugadores.isEmpty()) {
                    // Opcional: listar nombres o toString de cada usuario
                    String listaNombres = jugadores.stream()
                            .map(u -> {
                                // Asumimos que Usuario tiene un toString que muestra un identificador o nombre
                                String s = u.toString();
                                return s != null ? s : "Usuario";
                            })
                            .collect(Collectors.joining(", "));
                    System.out.println("    Lista de jugadores: " + listaNombres);
                }
            } else {
                System.out.println("  JugadoresParticipan: N/A");
            }

            // Nivel
            Nivel nivel = p.getNivel();
            System.out.println("  Nivel: " + (nivel != null ? nivel : "N/A"));

            System.out.println(); // Línea en blanco entre partidos
            idx++;
        }
        System.out.println("----- Fin de lista -----");
    }

}
