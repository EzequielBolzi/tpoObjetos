package com.tpo.armarPartido.controller;

import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUsuario {
    private final UsuarioRepository usuarioRepository;

    public ControllerUsuario() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void crearUsuario(UsuarioDTO usuarioDTO, String contrasena) {
        Usuario nuevo = DTOMapper.toUsuario(usuarioDTO, contrasena);
        usuarioRepository.save(nuevo);
        System.out.println("Se creó el usuario: " + nuevo.getNombre());
    }

    public void crearUsuario(String nombre, String correo, String contrasena,
                             Map<Deporte, Nivel> nivelesPorDeporte,
                             MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        Usuario nuevo = new Usuario(nombre, correo, contrasena, nivelesPorDeporte, medioNotificacion, ubicacion);
        usuarioRepository.save(nuevo);
        System.out.println("Se creó el usuario: " + nuevo.getNombre());
    }

    public void eliminarUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            System.out.println("Usuario eliminado: " + usuario.getNombre());
        }
    }

    public void modificarUsuario(String correo, UsuarioDTO usuarioDTO, String contrasena) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            Usuario actualizado = DTOMapper.toUsuario(usuarioDTO, contrasena);
            actualizado.setId(usuarioExistente.getId());
            usuarioRepository.save(actualizado);
            System.out.println("Usuario modificado: " + actualizado.getNombre());
        }
    }

    public void modificarUsuario(String correo, Usuario usuarioModificado) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            usuarioModificado.setId(usuarioExistente.getId());
            usuarioRepository.save(usuarioModificado);
            System.out.println("Usuario modificado: " + usuarioModificado.getNombre());
        }
    }

    public List<UsuarioDTO> getUsuariosDTO() {
        return usuarioRepository.findAll().stream().map(DTOMapper::toUsuarioDTO).collect(Collectors.toList());
    }

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioDTO getUsuarioDTOPorNombre(String nombre) {
        Usuario usuario = usuarioRepository.findByNombre(nombre);
        return usuario != null ? DTOMapper.toUsuarioDTO(usuario) : null;
    }
    
    public void agregarDeporteNivel(String correo, Deporte deporteNuevo, Nivel nivelDeDeporte) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuario.getNivelesPorDeporte().put(deporteNuevo, nivelDeDeporte);
            usuarioRepository.save(usuario);
            System.out.println("Se agregó a " + usuario.getNombre() + " el deporte " + deporteNuevo + " con el nivel " + nivelDeDeporte);
        }
    }
    
    public Usuario getUsuarioPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public List<UsuarioDTO> getAllUsuarios() {
        return getUsuariosDTO();
    }

    public UsuarioDTO getUsuarioByNombre(String nombre) {
        return getUsuarioDTOPorNombre(nombre);
    }

    public void createUsuario(UsuarioDTO usuarioDTO, String contrasena) {
        crearUsuario(usuarioDTO, contrasena);
    }

    public void deleteUsuario(String correo) {
        eliminarUsuario(correo);
    }
}
