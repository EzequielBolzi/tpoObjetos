package com.tpo.armarPartido.dto;

import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.model.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DTOMapper {
    
    public static UsuarioDTO toUsuarioDTO(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setNivelesPorDeporte(usuario.getNivelesPorDeporte());
        dto.setMedioNotificacion(usuario.getMedioNotificacion());
        dto.setUbicacion(usuario.getUbicacion());
        return dto;
    }

    
    public static Usuario toUsuario(UsuarioDTO dto, String contrasena) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNombre(dto.getNombre());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(contrasena);
        usuario.setNivelesPorDeporte(dto.getNivelesPorDeporte());
        usuario.setMedioNotificacion(dto.getMedioNotificacion());
        usuario.setUbicacion(dto.getUbicacion());
        return usuario;
    }

    
    public static PartidoDTO toPartidoDTO(Partido partido) {
        if (partido == null) return null;
        List<UsuarioDTO> jugadoresDTO = partido.getJugadoresParticipan() == null ? null :
            partido.getJugadoresParticipan().stream().map(DTOMapper::toUsuarioDTO).collect(Collectors.toList());
        PartidoDTO dto = new PartidoDTO(
            partido.getDeporte(),
            partido.getCantidadJugadores(),
            partido.getDuracion(),
            partido.getUbicacion(),
            partido.getHorario(),
            partido.getEstadoNombre(),
            partido.getNivel(),
            jugadoresDTO
        );
        dto.setId(partido.getId());
        return dto;
    }

    
    public static ComentarioDTO toComentarioDTO(Comentario comentario) {
        if (comentario == null) return null;
        return new ComentarioDTO(
            comentario.getJugador() != null ? comentario.getJugador().getNombre() : null,
            comentario.getComentario()
        );
    }

    
    public static Partido toPartido(PartidoDTO dto) {
        if (dto == null) return null;
        Partido partido = new Partido();
        partido.setId(dto.getId());
        partido.setDeporte(dto.getDeporte());
        partido.setCantidadJugadores(dto.getCantidadJugadores());
        partido.setDuracion(dto.getDuracion());
        partido.setUbicacion(dto.getUbicacion());
        partido.setHorario(dto.getHorario());
        partido.setNivel(dto.getNivel());
        partido.setEstadoNombre(dto.getEstado() != null ? dto.getEstado() : "NecesitamosJugadores");
        
        return partido;
    }

} 