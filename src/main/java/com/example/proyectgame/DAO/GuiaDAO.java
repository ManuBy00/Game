package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Model.Guia;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuiaDAO implements DAOinterface<Guia>{

    private static final String SQL_UPDATE = "UPDATE guia SET titulo = ?, cuerpo = ?, fechaPublicacion = ?, autorId = ?, videojuegoId = ? WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO guia (titulo, cuerpo, fechaPublicacion, autorId, videojuegoId) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM guia WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM guia";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM guia WHERE id = ?";

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    /**
     * Inserta una guía en la base de datos.
     * @param guia objeto guia a insertar
     * @return true si se ha insertado correctamente
     */
    public boolean insert(Guia guia) {
        boolean added = false;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, guia.getTitulo());
            ps.setString(2, guia.getCuerpo()); // NUEVO
            ps.setDate(3, Date.valueOf(guia.getFechaPublicacion()));
            ps.setInt(4, guia.getAutor().getId());
            ps.setInt(5, guia.getVideojuego().getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    guia.setId(rs.getInt(1));
                }
                added = true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return added;
    }

    /**
     * Elimina una guía por su ID.
     * @param id id de la guía a eliminar
     * @return true si se elimina correctamente
     */
    public boolean delete(int id) {
        boolean deleted = false;
        if (findById(id) != null) {
            try {
                PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(SQL_DELETE_BY_ID);
                pst.setInt(1, id);
                pst.executeUpdate();
                deleted = true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return deleted;
    }

    /**
     * Actualiza una guía con los datos nuevos.
     * @param guiaNueva objeto con datos nuevos
     * @param guiaActual objeto con datos antiguos (se usa para obtener el id)
     * @return true si se actualiza correctamente
     */
    public boolean update(Guia guiaNueva, Guia guiaActual) {
        boolean result = false;
        if (guiaNueva != null && guiaActual != null) {
            try {
                PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(SQL_UPDATE);

                pst.setString(1, guiaNueva.getTitulo());
                pst.setString(2, guiaNueva.getCuerpo()); // NUEVO
                pst.setDate(3, Date.valueOf(guiaNueva.getFechaPublicacion()));
                pst.setInt(4, guiaNueva.getAutor().getId());
                pst.setInt(5, guiaNueva.getVideojuego().getId());
                pst.setInt(6, guiaActual.getId());

                int filas = pst.executeUpdate();

                result = filas > 0;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Busca todas las guías de la base de datos.
     * @return lista de guías
     */
    public List<Guia> findAll() {
        List<Guia> guias = new ArrayList<>();
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Guia guia = new Guia();
                guia.setId(rs.getInt("id"));
                guia.setTitulo(rs.getString("titulo"));
                guia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                guia.setCuerpo(rs.getString("cuerpo"));

                Usuario autor = usuarioDAO.findById(rs.getInt("autorId"));
                guia.setAutor(autor);

                Videojuego videojuego = videojuegoDAO.findById(rs.getInt("videojuegoId"));
                guia.setVideojuego(videojuego);

                guias.add(guia);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guias;
    }

    /**
     * Busca una guía por su ID.
     * @param id id de la guía
     * @return objeto Guia o null si no existe
     */
    public Guia findById(int id) {
        Guia guia = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                guia = new Guia();
                guia.setId(rs.getInt("id"));
                guia.setTitulo(rs.getString("titulo"));
                guia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                guia.setCuerpo(rs.getString("cuerpo"));


                Usuario autor = usuarioDAO.findById(rs.getInt("autorId"));
                guia.setAutor(autor);

                Videojuego videojuego = videojuegoDAO.findById(rs.getInt("videojuegoId"));
                guia.setVideojuego(videojuego);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return guia;
    }
}
