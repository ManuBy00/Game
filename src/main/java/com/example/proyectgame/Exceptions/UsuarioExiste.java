package com.example.proyectgame.Exceptions;

public class UsuarioExiste extends RuntimeException {
  public UsuarioExiste(String message) {
    super(message);
  }
}
