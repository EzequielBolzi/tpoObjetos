# Migración a Persistencia con Base de Datos (Spring Data MongoDB)

Este documento describe los pasos necesarios para migrar la aplicación de almacenamiento en memoria a persistencia en base de datos utilizando Spring Data MongoDB.

---

## 1. Agregar dependencias en `pom.xml`

Asegúrate de incluir la siguiente dependencia en tu archivo `pom.xml`:

- Spring Boot Starter Data MongoDB

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

---

## 2. Configurar la base de datos

Agrega la configuración de la base de datos en `src/main/resources/application.properties` o `application.yml`.

Ejemplo:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/tu_nombre_db
```

---

## 3. Anotar las entidades

Modifica las clases de modelo (`Usuario`, `Partido`, etc.) para que sean documentos de MongoDB:

- Agrega `@Document(collection = "nombre_coleccion")` en la clase.
- Usa `@Id` de `org.springframework.data.annotation.Id`.
- No necesitas estrategias de generación, MongoDB usa ObjectId por defecto.

Ejemplo:

```java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    // ... otros campos ...
}
```

---

## 4. Crear interfaces Repository

Crea una interfaz para cada entidad que extienda `MongoRepository`.

Ejemplo:

```java
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    Usuario findByCorreo(String correo);
    Usuario findByNombre(String nombre);
}
```

---

## 5. Inyectar y usar los repositories en los controladores/servicios

- Reemplaza las listas en memoria por el uso de los repositories.
- Usa los métodos de los repositories para guardar, buscar, modificar y eliminar entidades.

Ejemplo:

```java
@Autowired
private UsuarioRepository usuarioRepository;

usuarioRepository.save(usuario);
usuarioRepository.findByCorreo(correo);
```

---

## 6. Adaptar la lógica de negocio

- Asegúrate de que toda la lógica que antes usaba listas ahora utilice los métodos de los repositories.
- Ajusta los endpoints REST para trabajar con la base de datos.

---

## 7. Probar la aplicación

- Ejecuta la aplicación y verifica que los datos se persisten correctamente en MongoDB.
- Usa MongoDB Compass o la CLI de MongoDB para inspeccionar los datos.

---

## 8. (Opcional) Configuración avanzada

- Configura usuarios, autenticación, o replica sets según tus necesidades de producción.

---

**¡Listo!**

Sigue estos pasos para migrar tu aplicación a persistencia real usando Spring Data MongoDB.
