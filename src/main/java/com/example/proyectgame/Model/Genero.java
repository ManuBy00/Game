package com.example.proyectgame.Model;

public enum Genero {
    ACCION ("Acción"), RPG("RPG"), SUPERVIVENCIA("Supervivencia"), AVENTURA("Aventura"), FANTASIA("Fantasía"), SHOOTER("Shooter"), DEPORTE("Deporte");

    private final String etiqueta;

    public String getEtiqueta() {
        return etiqueta;
    }

    Genero(String etiqueta){
        this.etiqueta = etiqueta;
    }
}
