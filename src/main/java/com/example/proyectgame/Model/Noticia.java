package com.example.proyectgame.Model;

import java.time.LocalDate;

public class Noticia extends Contenido {
    private String subtitulo;

    public Noticia(String titulo, LocalDate fechaPublicacion, String cuerpo, Usuario autor, String subtitulo) {
        super(titulo, fechaPublicacion, autor, cuerpo);
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
