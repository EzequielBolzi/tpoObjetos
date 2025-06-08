package com.tpo.armarPartido.model;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    private double latitud;
    private double longitud;


    public boolean esValida() {
        return latitud >= -90 && latitud <= 90 &&
                longitud >= -180 && longitud <= 180;
    }
}
