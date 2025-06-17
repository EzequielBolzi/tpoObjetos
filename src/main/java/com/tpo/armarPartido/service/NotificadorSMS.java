package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Usuario;

public class NotificadorSMS implements Notificador {
    @Override
    public void notificar(String mensaje, Usuario usuario) {
        System.out.println("[SMS] a " + usuario.getNombre() + ": " + mensaje);
    }
} 