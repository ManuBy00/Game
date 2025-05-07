package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Model.JuegoUsuario;
import com.example.proyectgame.Utilities.Sesion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JuegoUsuarioDAO implements DAOinterface <JuegoUsuario>{


    private static final String SQL_INSERT = "INSERT INTO juegoUsuario (usuarioId, videojuegoId, favorito, fechaAgregado) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM juegoUsuario WHERE usuarioId = ? AND videojuegoId = ?";
    private static final String SQL_UPDATE = "UPDATE juegoUsuario SET favorito = ?, fechaAgregado = ? WHERE usuarioId = ? AND videojuegoId = ?";
    private static final String SQL_FIND_BY_USER_ID = "SELECT * FROM juegoUsuario WHERE usuarioId = ?";
    private static final String SQL_FIND = "SELECT * FROM juegoUsuario WHERE usuarioId = ? AND videojuegoId = ?";
    private static final String SQL_FIND_BY_USUARIO = "SELECT * FROM juegoUsuario WHERE usuarioId = ?";

    public boolean insert(JuegoUsuario ju) {
        boolean added = false;
        if (ju != null) {
            try {
                Connection con = ConnectionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(SQL_INSERT);
                ps.setInt(1, ju.getUsuario().getId());
                ps.setInt(2, ju.getVideojuego().getId());
                ps.setBoolean(3, ju.isFavorito());
                ps.setDate(4, Date.valueOf(ju.getFechaAgregado()));

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


    public boolean delete(int videojuegoId) {
        boolean deleted = false;
        int usuarioId = Sesion.getInstancia().getUsuarioIniciado().getId();
        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, videojuegoId);

            deleted = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return deleted;
    }

    public boolean update(JuegoUsuario actual, JuegoUsuario nuevo) {
        boolean updated = false;

        if (actual != null && nuevo != null) {

            try (Connection con = ConnectionBD.getConnection();
                 PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

                // Nuevos valores
                ps.setBoolean(1, nuevo.isFavorito());
                ps.setDate(2, Date.valueOf(nuevo.getFechaAgregado()));

                // Condiciones para localizar el registro actual
                ps.setInt(3, actual.getUsuario().getId());
                ps.setInt(4, actual.getVideojuego().getId());

                int filas = ps.executeUpdate();
                updated = filas > 0;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return updated;
    }

    public JuegoUsuario findById(int id) {
        return null;
    }

    public static List<JuegoUsuario> findByUsuario(int usuarioId) { //mostrar biblioteca
        List<JuegoUsuario> lista = new ArrayList<>();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_USUARIO)) {

            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JuegoUsuario ju = new JuegoUsuario();
                    ju.setUsuario(usuarioDAO.findById(rs.getInt("usuarioId")));
                    ju.setVideojuego(videojuegoDAO.findById(rs.getInt("videojuegoId")));
                    ju.setFavorito(rs.getBoolean("favorito"));
                    ju.setFechaagregado(rs.getDate("fechaAgregado").toLocalDate());
                    lista.add(ju);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }
}
