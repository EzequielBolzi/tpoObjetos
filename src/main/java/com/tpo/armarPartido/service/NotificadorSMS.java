package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class NotificadorSMS implements Notificador {
    @Override
    public void notificar(String mensaje, Usuario usuario) {
        System.out.println("[SMS] a " + usuario.getNombre() + ": " + mensaje);
    }
} 