package com.tpo.armarPartido.service;

import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacionService {
    @Autowired
    private List<Notificador> notificadores;

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