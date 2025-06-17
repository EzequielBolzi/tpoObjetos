package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.model.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificadorMail implements Notificador {
    @Autowired
    private AdapterMail adapterMail;
    @Override
    public void notificar(String mensaje, Usuario usuario) {
        adapterMail.notificar(new Notificacion(mensaje, usuario));
    }
} 