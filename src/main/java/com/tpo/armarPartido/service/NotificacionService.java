package com.tpo.armarPartido.service;

import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.model.Usuario;
import java.util.List;

public class NotificacionService {
    private List<Notificador> notificadores;

    public NotificacionService(List<Notificador> notificadores) {
        this.notificadores = notificadores;
    }

    public void notificarPorMedio(String mensaje, Usuario usuario) {
        for (Notificador notificador : notificadores) {
            if (usuario.getMedioNotificacion() == MedioNotificacion.EMAIL && notificador instanceof NotificadorMail) {
                notificador.notificar(mensaje, usuario);
            }
            if (usuario.getMedioNotificacion() == MedioNotificacion.SMS && notificador instanceof NotificadorSMS) {
                notificador.notificar(mensaje, usuario);
            }
        }
    }
} 