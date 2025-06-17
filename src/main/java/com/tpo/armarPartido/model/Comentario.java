package com.tpo.armarPartido.model;

import jakarta.persistence.*;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario jugador;
    private String comentario;
    @ManyToOne
    @JoinColumn(name = "partido_id")
    private Partido partido;

    public Comentario() {}

    public Comentario(Usuario jugador, String comentario, Partido partido) {
        this.jugador = jugador;
        this.comentario = comentario;
        this.partido = partido;
    }

    public Usuario getJugador() {
        return jugador;
    }

    public void setJugador(Usuario jugador) {
        this.jugador = jugador;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }
}