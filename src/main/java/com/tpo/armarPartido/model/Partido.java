package com.tpo.armarPartido.model;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
import com.tpo.armarPartido.dto.UsuarioDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partido {

    private Deporte deporte;
    private int cantidadJugadores;
    private int duracion;
    private Ubicacion ubicacion;
    private Date horario;
    private EstadoPartido estado;
    private EstrategiaEmparejamiento emparejamiento;
    private List<UsuarioDTO> jugadoresParticipan;
    private Nivel nivel;
    private List<iObserver> observadores;



    public void cambiarEstado(EstadoPartido nuevo) {
        EstadoPartido estadoAnterior = this.estado;
        this.estado = nuevo;

        // Notificar cambio de estado
        notificarObservadores();

    }
    public void emparejarJugadores() {
        if (emparejamiento == null) {
            throw new IllegalStateException("No se ha definido una estrategia de emparejamiento");
        }

        try {
            this.jugadoresParticipan = emparejamiento.emparejar(this, this.jugadoresParticipan);

            // Cambiar estado  después del emparejamiento
            if (this.jugadoresParticipan.size() == this.cantidadJugadores) {
                cambiarEstado(EstadoPartido);
            }

        } catch (Exception e) {
            System.err.printf("Error al emparejar jugadores:", e.getMessage());
            cambiarEstado(EstadoPartido);
            throw e;
        }
    }

    public void agregarObservador(iObserver o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    public void quitarObservador(iObserver o) {
        observadores.remove(o);
    }

    public void notificarObservadores() {
        for (iObserver o : observadores) {
            try {
                o.update(new Notificacion(this));
            } catch (Exception e) {
                System.err.printf("Error al notificar observador: %s%n", e.getMessage());
            }
        }
    }
    }
