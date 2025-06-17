package com.tpo.armarPartido.controller;

import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.tpo.armarPartido.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class ControllerUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private static ControllerUsuario instancia;

    private ControllerUsuario() {
    }

    public static ControllerUsuario getInstancia() {
        if (instancia == null) {
            instancia = new ControllerUsuario();
            System.out.println("Inicio Controlador de Usuarios");
        }
        return instancia;
    }

    public void crearUsuario(UsuarioDTO usuarioDTO, String contrasena) {
        Usuario nuevo = DTOMapper.toUsuario(usuarioDTO, contrasena);
        usuarioRepository.save(nuevo);
        System.out.println(" + Se creó el usuario: " + nuevo.getNombre());
    }

    public void crearUsuario(String nombre, String correo, String contrasena,
                             Map<Deporte, Nivel> nivelesPorDeporte,
                             MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        Usuario nuevo = new Usuario(nombre, correo, contrasena, nivelesPorDeporte, medioNotificacion, ubicacion);
        usuarioRepository.save(nuevo);
        System.out.println(" + Se creó el usuario: " + nuevo.getNombre());
    }

    public void eliminarUsuario(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
    }

    public void modificarUsuario(String correo, UsuarioDTO usuarioDTO, String contrasena) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            Usuario actualizado = DTOMapper.toUsuario(usuarioDTO, contrasena);
            actualizado.setId(usuarioExistente.getId());
            usuarioRepository.save(actualizado);
        }
    }

    public void modificarUsuario(String correo, Usuario usuarioModificado) {
        Usuario usuarioExistente = usuarioRepository.findByCorreo(correo);
        if (usuarioExistente != null) {
            usuarioModificado.setId(usuarioExistente.getId());
            usuarioRepository.save(usuarioModificado);
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
            System.out.println("Se agrego a " + usuario.getNombre() + " el deporte " + deporteNuevo + " con el nivel " + nivelDeDeporte);
        }
    }
    
    public Usuario getUsuarioPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        return getUsuariosDTO();
    }

    @GetMapping("/{nombre}")
    public UsuarioDTO getUsuarioByNombre(@PathVariable String nombre) {
        return getUsuarioDTOPorNombre(nombre);
    }

    @PostMapping
    public void createUsuario(@RequestBody UsuarioDTO usuarioDTO, @RequestParam String contrasena) {
        crearUsuario(usuarioDTO, contrasena);
    }

    @DeleteMapping("/{correo}")
    public void deleteUsuario(@PathVariable String correo) {
        eliminarUsuario(correo);
    }
}
