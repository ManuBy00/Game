package com.example.proyectgame.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Videojuego {
    private int id;
    private String titulo;
    private String descripcion;
    private String desarrolladora;
    private String genero;
    private LocalDate fechalanzamiento;
    private List<Resena> resenaList;


    public Videojuego(String titulo, String descripcion, String desarrolladora, String genero, LocalDate fechalanzamiento) {
        this.titulo = titulo;
        this.desarrolladora = desarrolladora;
        this.descripcion = descripcion;
        this.genero = genero;
        this.fechalanzamiento = fechalanzamiento;
        this.resenaList = new ArrayList<>();
    }

    public Videojuego() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDate getFechalanzamiento() {
        return fechalanzamiento;
    }

    public void setFechalanzamiento(LocalDate fechalanzamiento) {
        this.fechalanzamiento = fechalanzamiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesarrolladora() {
        return desarrolladora;
    }

    public void setDesarrolladora(String desarrolladora) {
        this.desarrolladora = desarrolladora;
    }
}
