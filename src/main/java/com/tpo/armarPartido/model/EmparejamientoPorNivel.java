// package com.tpo.armarPartido.model;
//
// import com.tpo.armarPartido.enums.Nivel;
// import com.tpo.armarPartido.service.EstrategiaEmparejamiento;
// import com.tpo.armarPartido.model.UsuarioDTO;
// import org.springframework.stereotype.Component;
//
// import java.util.Comparator;
// import java.util.List;
// import java.util.stream.Collectors;
//
// @Component
// public class EmparejamientoPorNivel implements EstrategiaEmparejamiento {
//
//     @Override
//     public List<UsuarioDTO> emparejar(Partido partido, List<UsuarioDTO> jugadores) {
//         Nivel nivelRequerido = partido.getNivel();
//
//         return jugadores.stream()
//                 .filter(jugador -> {
//                     Nivel nivelJugador = jugador.getNivel();
//                     return nivelJugador != null && nivelJugador == nivelRequerido;
//                 })
//                 .limit(partido.getCantidadJugadores())
//                 .collect(Collectors.toList());
//     }
//
//     @Override
//     public String getNombreEstrategia() {
//         return "Por Nivel de Habilidad";
//     }
// }