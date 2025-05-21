package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Exceptions.UsuarioExiste;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;


import java.sql.*;

public class UsuarioDAO implements DAOinterface<Usuario>{


    private final static String SQL_INSERT = "INSERT INTO usuario (nombre, email, contrasena, rol) VALUES (?, ?, ?, ?)";
    private final static String SQL_FIND_BY_NAME = "SELECT * FROM usuario WHERE nombre = ?";
    private final static String SQL_DELETE_BY_ID= "DELETE FROM usuario WHERE id = ?";
    private final static String SQL_UPDATE = "UPDATE usuario SET nombre = ?, email = ?, contrasena = ?, rol = ? WHERE id = ?";
    private final static String SQL_FIND_BY_EMAIL = "SELECT * FROM usuario WHERE email = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM usuario WHERE id = ?";

    /**
     * Registra un usuario en la base de datos comprobando que email
     * @return true si se añade, false si hay algún problema
     */
    public boolean insert(Usuario usuario) {
        boolean added = false;
        if(findByEmail(usuario.getEmail()) || findByName(usuario.getNombre()) != null) {
            throw new UsuarioExiste("El nombre o correo introducido ya existe en la base de datos.");
        }
            try{
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, usuario.getNombre());
                ps.setString(2, usuario.getEmail());
                ps.setString(3,usuario.getContrasena());
                ps.setString(4,usuario.getRolUsuario().toString().toLowerCase());
                int filas = ps.executeUpdate(); //filas afectadas

                if (filas > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        usuario.setId(idGenerado);
                    }
                    added = true;
                }

            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        return added;
    }

    /**
     * Metodo que elimina un autor de la base de datos si existe, buscándolo por su Nombre.
     * @param id: objeto que contiene el Nombre del autor a eliminar
     * @return true si ha encontrado y eliminado correctamente el autor, false si no se ha podido eliminar
     */
    public boolean delete(int id) {
        boolean deleted = false;
        if (findById(id)!=null) {
            try{
                PreparedStatement pst= ConnectionBD.getConnection().prepareStatement(SQL_DELETE_BY_ID);
                pst.setInt(1, id);
                pst.executeUpdate();
                deleted = true;
            }catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return deleted;
    }

    /**
     * Actualiza los datos del usuario logeado
     * @param usuarioNuevo objeto usuario con los datos nuevos
     * @return true si se actualiza correctamente
     */
    public boolean update(Usuario usuarioNuevo, Usuario usuarioActual) {
        boolean result = false;
        if((usuarioNuevo!=null)) {
            try {
                PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(SQL_UPDATE);
                pst.setString(1, usuarioNuevo.getNombre());
                pst.setString(2, usuarioNuevo.getEmail());
                pst.setString(3, usuarioNuevo.getContrasena());
                pst.setString(4, usuarioNuevo.getRolUsuario().toString().toLowerCase());
                pst.setInt(5, usuarioActual.getId());
                int filas = pst.executeUpdate();


                result = filas > 0;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Busca en la base de datos un ususario a través de su nombre
     * @param nombreUsuario nombre del usuario a buscar
     * @return usuario encontrado, null si no existe
     */
    public static Usuario findByName(String nombreUsuario) {
        Usuario usuario = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_NAME);
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setRolUsuario(RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    /**
     * Busca en la base de datos un ususario a través de su nombre
     * @param email correo del usuario a buscar
     * @return true si existe
     */
    public static boolean findByEmail(String email) {
        boolean encontrado = false;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                encontrado = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return encontrado;
    }

    /**
     * Busca en la base de datos un usuario a través de su ID
     * @param idUsuario ID del usuario a buscar
     * @return usuario encontrado, null si no existe
     */
    public Usuario findById(int idUsuario) {
        Usuario usuario = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setEmail(rs.getString("email"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setRolUsuario(RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return usuario;
    }

    /**
     *
     * @param correo correo del usuario que se va a logear
     * @param password contraseña sin hashear del usuario que se va a logear
     * @return usuario validado
     */
    public static Usuario login(String correo, String password){
        Usuario usuarioValidado = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_EMAIL);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuarioValidado = new Usuario();
                usuarioValidado.setId(rs.getInt("id"));
                usuarioValidado.setNombre(rs.getString("nombre"));
                usuarioValidado.setEmail(rs.getString("email"));
                usuarioValidado.setContrasena(rs.getString("contrasena"));
                usuarioValidado.setRolUsuario(RolUsuario.valueOf(rs.getString("rol").toUpperCase()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (usuarioValidado !=null && usuarioValidado.verificarPassword(password)){
            return usuarioValidado;
        }else{
            return null;
        }
    }


}
