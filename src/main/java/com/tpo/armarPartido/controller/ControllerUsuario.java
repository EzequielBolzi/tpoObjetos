package com.tpo.armarPartido.controller;

import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.repository.UsuarioRepository;
import com.tpo.armarPartido.exception.ValidationException;
import com.tpo.armarPartido.exception.UsuarioNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerUsuario {
    private final UsuarioRepository usuarioRepository;

    public ControllerUsuario() {
        this.usuarioRepository = new UsuarioRepository();
    }

    private void validarCamposUsuario(String nombre, String correo, String contrasena, 
                                     MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        List<String> errores = new ArrayList<>();
        
        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre no puede estar vacío");
        }
        if (correo == null || correo.trim().isEmpty()) {
            errores.add("El correo no puede estar vacío");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            errores.add("La contraseña no puede estar vacía");
        }
        if (medioNotificacion == null) {
            errores.add("El medio de notificación no puede estar vacío");
        }
        if (ubicacion == null) {
            errores.add("La ubicación no puede estar vacía");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException("Errores de validación en usuario", errores);
        }
    }

    public void crearUsuario(UsuarioDTO usuarioDTO, String contrasena) {
        validarCamposUsuario(usuarioDTO.getNombre(), usuarioDTO.getCorreo(), contrasena, 
                           usuarioDTO.getMedioNotificacion(), usuarioDTO.getUbicacion());
        Usuario nuevo = DTOMapper.toUsuario(usuarioDTO, contrasena);
        usuarioRepository.save(nuevo);
        System.out.println("Se creó el usuario: " + nuevo.getNombre());
    }

    public void crearUsuario(String nombre, String correo, String contrasena,
                             Map<Deporte, Nivel> nivelesPorDeporte,
                             MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        validarCamposUsuario(nombre, correo, contrasena, medioNotificacion, ubicacion);
        Usuario nuevo = new Usuario(nombre, correo, contrasena, nivelesPorDeporte, medioNotificacion, ubicacion);
        usuarioRepository.save(nuevo);
        System.out.println("Se creó el usuario: " + nuevo.getNombre());
    }

    public void eliminarUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
            System.out.println("Usuario eliminado: " + usuario.getNombre());
        } else {
            throw new UsuarioNotFoundException(correo);
        }
    }

    public void modificarUsuario(String correo, UsuarioDTO usuarioDTO, String contrasena) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            Usuario actualizado = DTOMapper.toUsuario(usuarioDTO, contrasena);
            actualizado.setId(usuarioExistente.getId());
            usuarioRepository.save(actualizado);
            System.out.println("Usuario modificado: " + actualizado.getNombre());
        } else {
            throw new UsuarioNotFoundException("No se puede modificar", correo);
        }
    }

    public void modificarUsuario(String correo, Usuario usuarioModificado) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            usuarioModificado.setId(usuarioExistente.getId());
            usuarioRepository.save(usuarioModificado);
            System.out.println("Usuario modificado: " + usuarioModificado.getNombre());
        } else {
            throw new UsuarioNotFoundException("No se puede modificar", correo);
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
        } else {
            throw new UsuarioNotFoundException("No se puede agregar deporte", correo);
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
