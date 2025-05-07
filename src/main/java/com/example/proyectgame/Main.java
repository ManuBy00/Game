package com.example.proyectgame;

import com.example.proyectgame.DAO.ResenaDAO;
import com.example.proyectgame.DAO.UsuarioDAO;
import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.*;


public class Main {
    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
        ResenaDAO resenaDAO = new ResenaDAO();



        if (usuarioDAO.delete(10)) {
            System.out.println("Usuario insertado");
        }else {
            System.out.println("Usuario no insertado");
        }
    }
}
