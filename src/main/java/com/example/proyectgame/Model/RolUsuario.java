package com.example.proyectgame.Model;

public enum RolUsuario {
    ADMINISTRADOR ("Administrador"), JUGADOR("Jugador");

    private final String etiqueta;

    RolUsuario(String etiqueta){
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }


}
