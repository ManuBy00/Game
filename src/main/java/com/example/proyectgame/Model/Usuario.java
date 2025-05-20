package com.example.proyectgame.Model;

import com.example.proyectgame.Utilities.Seguridad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String contrasena;
    private Enum RolUsuario; // 'administrador' o 'jugador'


    public Usuario(String nombre, String email, String contrasena, RolUsuario rol) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = Seguridad.hashPassword(contrasena);
        this.RolUsuario = rol;
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Enum getRolUsuario() {
        return RolUsuario;
    }

    public void setRolUsuario(Enum rolUsuario) {
        RolUsuario = rolUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Verifica la contraseña con su respectivo hash
     * @param password
     * @return true si coincide, false si no.
     */
    public boolean verificarPassword(String password){
        return Seguridad.checkPassword(password, this.contrasena);
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Usuario usuario)) return false;
        return Objects.equals(email, usuario.email) && Objects.equals(contrasena, usuario.contrasena);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, contrasena);
    }
}
