package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Model.Noticia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticiaDAO implements DAOinterface<Noticia> {
    private final static String SQL_INSERT = "INSERT INTO noticia (titulo, contenido, fechaPublicacion, autor) VALUES (?, ?, ?, ?)";
    private final static String SQL_DELETE_BY_ID = "DELETE FROM noticia WHERE id = ?";
    private final static String SQL_UPDATE = "UPDATE noticia SET titulo = ?, contenido = ?, fechaPublicacion = ? WHERE id = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM noticia WHERE id = ?";
    private final static String SQL_FIND_ALL = "SELECT * FROM noticia";
    private final static String SQL_FIND_BY_TITULO = "SELECT * FROM noticia WHERE titulo = ?";

    public  boolean insert(Noticia noticia) {
        boolean added = false;
        if (noticia != null && findByTituloLazy(noticia.getTitulo())==null) {
            try {
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_INSERT);
                ps.setString(1, noticia.getTitulo());
                ps.setString(2, noticia.getContenido());
                ps.setDate(3, Date.valueOf(noticia.getFechaPublicacion()));
                ps.setInt(4, noticia.getAutor().getId());

                int filas = ps.executeUpdate();
                if (filas > 0) {
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

    public boolean update(Noticia noticiaNueva, Noticia noticiaActual) {
        boolean updated = false;
        if (noticiaActual != null && findById(noticiaActual.getId())!=null) {
            try {
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_UPDATE);
                ps.setString(1, noticiaActual.getTitulo());
                ps.setString(2, noticiaActual.getContenido());
                ps.setDate(3, Date.valueOf(noticiaActual.getFechaPublicacion()));
                ps.setInt(4, noticiaNueva.getId());

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

    public Noticia findById(int id) {
        Noticia noticia = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                noticia = new Noticia();
                noticia.setId(rs.getInt("id"));
                noticia.setTitulo(rs.getString("titulo"));
                noticia.setContenido(rs.getString("contenido"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noticia;
    }

    public static List<Noticia> findAll() {
        List<Noticia> noticias = new ArrayList<>();
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Noticia noticia = new Noticia();
                noticia.setId(rs.getInt("id"));
                noticia.setTitulo(rs.getString("titulo"));
                noticia.setContenido(rs.getString("contenido"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                noticias.add(noticia);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noticias;
    }

    public static Noticia findByTituloLazy(String titulo) {
        Noticia noticia = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_TITULO);
            ps.setString(1, titulo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                noticia = new Noticia();
                noticia.setId(rs.getInt("id"));
                noticia.setTitulo(rs.getString("titulo"));
                noticia.setContenido(rs.getString("contenido"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                noticia.setAutor(null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noticia;
    }
}
