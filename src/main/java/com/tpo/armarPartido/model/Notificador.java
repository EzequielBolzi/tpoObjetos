package com.tpo.armarPartido.model;

import com.tpo.armarPartido.enums.MedioNotificacion;
import com.tpo.armarPartido.service.EstrategiaNotificacion;
import com.tpo.armarPartido.service.EstrategiaNotificacionSMS;
import com.tpo.armarPartido.service.EstrategiaNotificacionMail;
import com.tpo.armarPartido.service.AdapterNotificacionMail;
import com.tpo.armarPartido.service.iObserver;
import java.time.LocalDateTime;

public class Notificador implements iObserver {
    private EstrategiaNotificacion estrategiaDeNotificacion;
    private AdapterNotificacionMail adapterNotificacionMail;

    public Notificador() {}

    public Notificador(AdapterNotificacionMail adapterNotificacionMail) {
        this.adapterNotificacionMail = adapterNotificacionMail;
    }

    public Notificador(EstrategiaNotificacion estrategiaDeNotificacion, AdapterNotificacionMail adapterNotificacionMail) {
        this.estrategiaDeNotificacion = estrategiaDeNotificacion;
        this.adapterNotificacionMail = adapterNotificacionMail;
    }

    public EstrategiaNotificacion getEstrategiaDeNotificacion() {
        return estrategiaDeNotificacion;
    }

    public void setEstrategiaDeNotificacion(EstrategiaNotificacion estrategiaDeNotificacion) {
        this.estrategiaDeNotificacion = estrategiaDeNotificacion;
    }

    public AdapterNotificacionMail getAdapterNotificacionMail() {
        return adapterNotificacionMail;
    }

    public void setAdapterNotificacionMail(AdapterNotificacionMail adapterNotificacionMail) {
        this.adapterNotificacionMail = adapterNotificacionMail;
    }

    @Override
    public void actualizar(Partido partido) {
        enviarNotificacionesAUsuarios(partido);
    }

    private void enviarNotificacionesAUsuarios(Partido partido) {
        if (partido != null && partido.getJugadoresParticipan() != null) {
            for (Usuario usuario : partido.getJugadoresParticipan()) {
                MedioNotificacion estrategiaUsuario = usuario.getMedioNotificacion();
                if (estrategiaUsuario != null) {
                    switch (estrategiaUsuario) {
                        case SMS:
                            this.estrategiaDeNotificacion = new EstrategiaNotificacionSMS();
                            break;
                        case EMAIL:
                            this.estrategiaDeNotificacion = new EstrategiaNotificacionMail(adapterNotificacionMail);
                            break;
                        default:
                            System.out.println("Medio de notificación no soportado: " + estrategiaUsuario);
                            continue;
                    }
                    estrategiaDeNotificacion.enviarNotificacion(
                            new Notificacion(usuario, partido, partido.getEstado().getMessage(partido), "ESTADO", LocalDateTime.now())
                    );
                }
            }
        }
    }

    public void cambiarEstrategiaNotificacion(EstrategiaNotificacion estrategiaNueva) {
        this.estrategiaDeNotificacion = estrategiaNueva;
    }
}