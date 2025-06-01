package com.tpo.armarPartido.service;

import com.tpo.armarPartido.model.Partido;

import java.util.List;

public interface EstrategiaEmparejamiento {


    List<UsuarioDTO> emparejar(Partido partido, List<UsuarioDTO> jugadores);

    String getNombreEstrategia();


}