package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Model.Resena;

import java.sql.*;

public class ResenaDAO implements DAOinterface <Resena>{
    private final static String SQL_INSERT = "INSERT INTO resena (comentario, puntuacion, usuarioid, videojuegoid) VALUES (?, ?, ?, ?)";
    private final static String SQL_DELETE_BY_ID = "DELETE FROM resena WHERE id = ?";
    private final static String SQL_UPDATE = "UPDATE resena SET comentario = ?, puntuacion = ?, usuarioid = ?, videojuegoid = ? WHERE id = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM resena WHERE id = ?";

    public  boolean insert(Resena resena) {
        boolean added = false;
        if (resena != null) {
            try {
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, resena.getComentario());
                ps.setInt(2, resena.getPuntuacion());
                ps.setInt(3, resena.getUsuario().getId());
                ps.setInt(4, resena.getVideojuego().getId());
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        resena.setId(rs.getInt(1)); // Asignar el ID generado a la reseña
                    }
                    added = true;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return added;
    }

    public boolean delete(int id) {
        boolean deleted = false;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_DELETE_BY_ID);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                deleted = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }

    public boolean update(Resena resenaNueva, Resena resenaActual) {
        boolean updated = false;
        if (resenaNueva != null && findByIdLazy(resenaActual.getId())!=null) { //usamos la versión lazy porque solo necesitamos saber si la reseña existe
            try {
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_UPDATE);
                ps.setString(1, resenaNueva.getComentario());
                ps.setInt(2, resenaNueva.getPuntuacion());
                ps.setInt(3, resenaNueva.getUsuario().getId());
                ps.setInt(4, resenaNueva.getVideojuego().getId());
                ps.setInt(5, resenaActual.getId());
                int filas = ps.executeUpdate();
                if (filas > 0) {
                    updated = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return updated;
    }

    public Resena findById(int id) { //eager
        Resena resena = null;
        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resena = new Resena();
                resena.setId(rs.getInt("id"));
                resena.setComentario(rs.getString("comentario"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setUsuario(usuarioDAO.findById(rs.getInt("usuarioid")));
                resena.setVideojuego(videojuegoDAO.findById(rs.getInt("videojuegoid")));
                resena.setComentario(rs.getString("fecha"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resena;
    }

    public static Resena findByIdLazy(int id) { //lazy
        Resena resena = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                resena = new Resena();
                resena.setId(rs.getInt("id"));
                resena.setComentario(rs.getString("comentario"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setUsuario(null);
                resena.setVideojuego(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resena;
    }
}
