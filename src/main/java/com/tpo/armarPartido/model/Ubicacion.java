package com.tpo.armarPartido.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Ubicacion {
    private double latitud;
    private double longitud;

    public Ubicacion() {}

    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Ubicacion [latitud=" + latitud + ", longitud=" + longitud + "]";
    }

    public double distanciaCuadradoA(Ubicacion otraUbicacion) {
        if (otraUbicacion == null) {
            throw new IllegalArgumentException("La ubicación de destino no puede ser null");
        }
        double dx = this.latitud - otraUbicacion.latitud;
        double dy = this.longitud - otraUbicacion.longitud;
        return dx * dx + dy * dy;
    }

    public double distanciaA(Ubicacion otraUbicacion) {
        return Math.sqrt(distanciaCuadradoA(otraUbicacion));
    }
}
