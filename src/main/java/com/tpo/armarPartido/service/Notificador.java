package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Usuario;

public interface Notificador {
    void notificar(String mensaje, Usuario usuario);
} 