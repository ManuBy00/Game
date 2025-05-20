package com.example.proyectgame.Model;

import java.time.LocalDate;

public class Noticia extends Contenido {
    private String subtitulo;

    public Noticia(String titulo, LocalDate fechaPublicacion, String portada, String cuerpo, Usuario autor, String subtitulo) {
        super(titulo, fechaPublicacion, portada, autor, cuerpo);
        this.subtitulo = subtitulo;
    }

    public Noticia(){

    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }
}
