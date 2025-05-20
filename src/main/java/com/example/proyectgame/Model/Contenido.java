package com.example.proyectgame.Model;

import com.example.proyectgame.Utilities.Sesion;

import java.time.LocalDate;

public abstract class Contenido {
    private int id;
    private String titulo;
    private LocalDate fechaPublicacion;
    private String portada;
    private Usuario autor;
    private String cuerpo;

    public Contenido(String titulo, LocalDate fechaPublicacion, String portada, Usuario autor, String cuerpo) {
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.portada = portada;
        this.autor = autor;
        this.cuerpo = cuerpo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Contenido() {
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }
}