package com.example.proyectgame;

import com.example.proyectgame.DAO.ResenaDAO;
import com.example.proyectgame.DAO.UsuarioDAO;
import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.*;

import java.net.URL;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
        ResenaDAO resenaDAO = new ResenaDAO();


        //Usuario usuario = new Usuario("ManuBy", "manuel@gmail.com", "12345678", RolUsuario.ADMINISTRADOR);

        String titulo = "Prueba16";
        String descripcion = "Videojuego de supervivencia";
        String desarrolladora = "Mojang";
        String genero = "Supervivencia";
        LocalDate fechaLanzamiento = LocalDate.of(2021, 5, 19);  // Fecha de lanzamiento (por ejemplo)
        String portada = "Minecraft.jpg";  // Nombre del archivo de la portada

        // Crear el objeto Videojuego usando el constructor completo
        Videojuego juego = new Videojuego(titulo, descripcion, desarrolladora, genero, fechaLanzamiento, portada);

        videojuegoDAO.insert(juego);



    }
}
