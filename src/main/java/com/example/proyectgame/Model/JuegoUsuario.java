package com.example.proyectgame.Model;

import java.time.LocalDate;

public class JuegoUsuario {
    private Usuario usuario;
    private Videojuego videojuego;
    private boolean favorito;
    private LocalDate fechaAgregado;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaagregado(LocalDate fechaagregado) {
        this.fechaAgregado = fechaagregado;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }
}
