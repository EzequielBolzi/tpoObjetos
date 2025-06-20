package com.tpo.armarPartido;

import com.tpo.armarPartido.model.Usuario;
import com.tpo.armarPartido.model.Partido;
import com.tpo.armarPartido.model.Ubicacion;
import com.tpo.armarPartido.enums.*;
import com.tpo.armarPartido.repository.UsuarioRepository;
import com.tpo.armarPartido.repository.PartidoRepository;
import com.tpo.armarPartido.repository.NotificacionRepository;
import com.tpo.armarPartido.repository.ComentarioRepository;
import com.tpo.armarPartido.service.EmparejamientoPorNivel;
import com.tpo.armarPartido.service.EmparejamientoPorUbicacion;
import com.tpo.armarPartido.service.estados.NecesitamosJugadores;
import com.tpo.armarPartido.model.Notificacion;
import com.tpo.armarPartido.model.Comentario;

import java.util.*;

public class DataInitializer {
    public static void init(UsuarioRepository usuarioRepo, PartidoRepository partidoRepo) {
        NotificacionRepository notificacionRepo = new NotificacionRepository();
        ComentarioRepository comentarioRepo = new ComentarioRepository();
        // 0. Limpiar notificaciones y comentarios
        for (Notificacion n : notificacionRepo.findAll()) notificacionRepo.delete(n);
        for (Comentario c : comentarioRepo.findAll()) comentarioRepo.delete(c);

        // 1. Limpiar
        for (Partido p : partidoRepo.findAll()) partidoRepo.delete(p);
        for (Usuario u : usuarioRepo.findAll()) usuarioRepo.delete(u);

        // 2. Crear usuarios
        Map<Deporte, Nivel> nivelesJuan = new HashMap<>();
        nivelesJuan.put(Deporte.FUTBOL, Nivel.INTERMEDIO);
        nivelesJuan.put(Deporte.BASQUET, Nivel.PRINCIPIANTE);

        Map<Deporte, Nivel> nivelesAna = new HashMap<>();
        nivelesAna.put(Deporte.VOLEY, Nivel.INTERMEDIO);
        nivelesAna.put(Deporte.FUTBOL, Nivel.INTERMEDIO);

        Map<Deporte, Nivel> nivelesLuis = new HashMap<>();
        nivelesLuis.put(Deporte.FUTBOL, Nivel.AVANZADO);
        nivelesLuis.put(Deporte.BASQUET, Nivel.AVANZADO);

        Map<Deporte, Nivel> nivelesSofia = new HashMap<>();
        nivelesSofia.put(Deporte.FUTBOL, Nivel.PRINCIPIANTE);
        nivelesSofia.put(Deporte.VOLEY, Nivel.INTERMEDIO);

        Map<Deporte, Nivel> nivelesMartin = new HashMap<>();
        nivelesMartin.put(Deporte.BASQUET, Nivel.INTERMEDIO);
        nivelesMartin.put(Deporte.VOLEY, Nivel.AVANZADO);

        Map<Deporte, Nivel> nivelesCarla = new HashMap<>();
        nivelesCarla.put(Deporte.FUTBOL, Nivel.INTERMEDIO);
        nivelesCarla.put(Deporte.BASQUET, Nivel.PRINCIPIANTE);
        nivelesCarla.put(Deporte.VOLEY, Nivel.PRINCIPIANTE);

        Map<Deporte, Nivel> nivelesEze = new HashMap<>();
        nivelesEze.put(Deporte.FUTBOL, Nivel.AVANZADO);

        usuarioRepo.save(new Usuario("Juan Perez", "juan@example.com", "1234", nivelesJuan, MedioNotificacion.EMAIL, new Ubicacion(1, 3)));
        usuarioRepo.save(new Usuario("Ana Gomez", "ana@example.com", "abcd", nivelesAna, MedioNotificacion.SMS, new Ubicacion(15, 16)));
        usuarioRepo.save(new Usuario("Luis Martinez", "luis@example.com", "xyz789", nivelesLuis, MedioNotificacion.SMS, new Ubicacion(5, 8)));
        usuarioRepo.save(new Usuario("Sofia Lopez", "sofia@example.com", "clave1", nivelesSofia, MedioNotificacion.EMAIL, new Ubicacion(4, 2)));
        usuarioRepo.save(new Usuario("Martin Ruiz", "martin@example.com", "pass456", nivelesMartin, MedioNotificacion.SMS, new Ubicacion(6, 1)));
        usuarioRepo.save(new Usuario("Carla Fernández", "carla@example.com", "segura321", nivelesCarla, MedioNotificacion.EMAIL, new Ubicacion(3, 5)));
        usuarioRepo.save(new Usuario("Eze B", "eze@example.com", "pass789", nivelesEze, MedioNotificacion.EMAIL, new Ubicacion(10, 10)));

        // 3. Crear partidos
        Calendar calendar = Calendar.getInstance();
        // Partido 1 - creado por Luis
        Usuario Luis = usuarioRepo.findByNombre("Luis Martinez");
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date horarioPartidoLuis = calendar.getTime();
        Partido partido1 = new Partido(Deporte.FUTBOL, 2, 10, new Ubicacion(1, 1), horarioPartidoLuis, new NecesitamosJugadores(), new EmparejamientoPorUbicacion(), Arrays.asList(Luis), Nivel.AVANZADO, Luis);
        partido1.setEmparejamientoTipo("ubicacion");
        partidoRepo.save(partido1);

        // Partido 2 - creado por Ana
        Usuario Ana = usuarioRepo.findByNombre("Ana Gomez");
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date horarioPartidoAna = calendar.getTime();
        Partido partido2 = new Partido(Deporte.VOLEY, 3, 10, new Ubicacion(1, 1), horarioPartidoAna, new NecesitamosJugadores(), new EmparejamientoPorNivel(), Arrays.asList(Ana), Nivel.INTERMEDIO, Ana);
        partido2.setEmparejamientoTipo("nivel");
        partidoRepo.save(partido2);

        // Partido 3 - creado por Sofia
        Usuario Sofia = usuarioRepo.findByNombre("Sofia Lopez");
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date horarioPartidoSofia = calendar.getTime();
        Partido partido3 = new Partido(Deporte.VOLEY, 2, 8, new Ubicacion(20, 20), horarioPartidoSofia, new NecesitamosJugadores(), new EmparejamientoPorUbicacion(), Arrays.asList(Sofia), Nivel.INTERMEDIO, Sofia);
        partido3.setEmparejamientoTipo("ubicacion");
        partidoRepo.save(partido3);

        // Partido 4 - creado por Martin
        Usuario Martin = usuarioRepo.findByNombre("Martin Ruiz");
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date horarioPartidoMartin = calendar.getTime();
        Partido partido4 = new Partido(Deporte.BASQUET, 2, 6, new Ubicacion(6, 1), horarioPartidoMartin, new NecesitamosJugadores(), new EmparejamientoPorNivel(), Arrays.asList(Martin), Nivel.INTERMEDIO, Martin);
        partido4.setEmparejamientoTipo("nivel");
        partidoRepo.save(partido4);

        // Partido 5 - creado por Eze
        Usuario Eze = usuarioRepo.findByNombre("Eze B");
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date horarioPartidoEze = calendar.getTime();
        Partido partido5 = new Partido(Deporte.FUTBOL, 2, 12, new Ubicacion(10, 10), horarioPartidoEze, new NecesitamosJugadores(), new EmparejamientoPorNivel(), Arrays.asList(Eze), Nivel.AVANZADO, Eze);
        partido5.setEmparejamientoTipo("nivel");
        partidoRepo.save(partido5);
    }
} 