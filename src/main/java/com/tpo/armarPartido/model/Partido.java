package com.tpo.armarPartido.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
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
    private List<Usuario> jugadoresParticipan;
    private Nivel nivel;
    private List<iObserver> observadores;

    public void cambiarEstado(EstadoPartido nuevo) {
        this.estado = nuevo;
    }

    public void emparejarJugadores() {
        this.jugadoresParticipan = emparejamiento.emparejar(this);
    }

    public void agregarObservador(iObserver o) {
        observadores.add(o);
    }

    public void quitarObservador(iObserver o) {
        observadores.remove(o);
    }

    public void notificarObservadores() {
        for (iObserver o : observadores) {
            o.update(new Notificacion(this));
        }
    }
}
