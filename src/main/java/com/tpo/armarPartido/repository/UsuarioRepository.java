package com.tpo.armarPartido.repository;

import com.tpo.armarPartido.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByCorreo(String correo);
    Usuario findByNombre(String nombre);
    // Métodos personalizados si los necesitas
} 