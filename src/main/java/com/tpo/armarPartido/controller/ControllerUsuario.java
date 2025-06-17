package com.tpo.armarPartido.controller;

import com.tpo.armarPartido.dto.UsuarioDTO;
import com.tpo.armarPartido.dto.DTOMapper;
import com.tpo.armarPartido.enums.Deporte;
import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.enums.Nivel;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class ControllerUsuario {
    private static ControllerUsuario instancia;
    private List<Usuario> usuarios;

    private ControllerUsuario() {
        usuarios = new ArrayList<>();
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
        usuarios.add(nuevo);
        System.out.println(" + Se creó el usuario: " + nuevo.getNombre());
    }

    public void crearUsuario(String nombre, String correo, String contrasena,
                             Map<Deporte, Nivel> nivelesPorDeporte,
                             MedioNotificacion medioNotificacion, Ubicacion ubicacion) {
        Usuario nuevo = new Usuario(nombre, correo, contrasena, nivelesPorDeporte, medioNotificacion, ubicacion);
        usuarios.add(nuevo);
        System.out.println(" + Se creó el usuario: " + nuevo.getNombre());
    }

    public void eliminarUsuario(String correo) {
        usuarios.removeIf(u -> u.getCorreo().equalsIgnoreCase(correo));
    }

    public void modificarUsuario(String correo, UsuarioDTO usuarioDTO, String contrasena) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCorreo().equalsIgnoreCase(correo)) {
                usuarios.set(i, DTOMapper.toUsuario(usuarioDTO, contrasena));
                return;
            }
        }
    }

    public void modificarUsuario(String correo, Usuario usuarioModificado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCorreo().equalsIgnoreCase(correo)) {
                usuarios.set(i, usuarioModificado);
                return;
            }
        }
    }

    public List<UsuarioDTO> getUsuariosDTO() {
        return usuarios.stream().map(DTOMapper::toUsuarioDTO).collect(Collectors.toList());
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public UsuarioDTO getUsuarioDTOPorNombre(String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equalsIgnoreCase(nombre)) {
                return DTOMapper.toUsuarioDTO(usuario);
            }
        }
        return null;
    }
    
    public void agregarDeporteNivel(String correo, Deporte deporteNuevo, Nivel nivelDeDeporte) {
    	for(Usuario usuario:usuarios) {
    		if(usuario.getCorreo().equalsIgnoreCase(correo)) {
    			usuario.getNivelesPorDeporte().put(deporteNuevo, nivelDeDeporte);
    			System.out.println("Se agrego a " + usuario.getNombre() + " el deporte " + deporteNuevo + " con el nivel " + nivelDeDeporte);
    		}
    	}
    }
    
    public Usuario getUsuarioPorNombre(String nombre) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equalsIgnoreCase(nombre)) {
                return usuario;
            }
        }
        return null;
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
