package com.example.proyectgame.DAO;

import com.example.proyectgame.Model.Videojuego;

public interface DAOinterface <T>{

    public boolean insert(T elemento);

    public boolean update(T elementoNuevo, T elementoActual);

    public boolean delete(int id);

    public T findById(int id);
}
