package com.example.proyectgame.Model;

import java.time.LocalDate;

public class Guia extends Contenido {
    private Videojuego videojuego;

    public Guia(String titulo, LocalDate fechaPublicacion, Usuario autor, String cuerpo, Videojuego videojuego) {
        super(titulo, fechaPublicacion, autor, cuerpo);
        this.videojuego = videojuego;
    }

    public Guia(){
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }
}