package com.tpo.armarPartido.service.estados;

import com.tpo.armarPartido.model.Partido;

public interface EstadoPartido {
    void cancelar(Partido partido);
    void armar(Partido partido);
    void confirmar(Partido partido);
    void comenzar(Partido partido);
    void finalizar(Partido partido);
}
