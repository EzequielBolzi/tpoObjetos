package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.model.Notificacion;
import com.tpo.armarPartido.model.Partido;
import java.time.LocalDateTime;

public class NotificadorMail implements Notificador, iObserver {
    private AdapterMail adapterMail;

    public NotificadorMail(AdapterMail adapterMail) {
        this.adapterMail = adapterMail;
    }

    @Override
    public void notificar(String mensaje, Usuario usuario) {
        adapterMail.notificar(new Notificacion(usuario, null, mensaje, "EMAIL", LocalDateTime.now()));
    }

    @Override
    public void actualizar(Partido partido) {
        if (partido.getJugadoresParticipan() != null && !partido.getJugadoresParticipan().isEmpty()) {
            Usuario creador = partido.getJugadoresParticipan().get(0);
            notificar("El partido ha cambiado de estado: " + partido.getEstado(), creador);
        }
    }
} 