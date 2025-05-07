package com.example.proyectgame.Model;

import java.time.LocalDate;

public class Resena {
    private int id;
    private Usuario usuario;
    private Videojuego videojuego;
    private int puntuacion;
    private String comentario;
    private LocalDate fechaPublicacion;

    public Resena(String comentario, int puntuacion, Videojuego videojuego, Usuario usuario) {
        this.comentario = comentario;
        this.puntuacion = puntuacion;
        this.videojuego = videojuego;
        this.usuario = usuario;
        this.fechaPublicacion = LocalDate.now();
    }

    public Resena() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
