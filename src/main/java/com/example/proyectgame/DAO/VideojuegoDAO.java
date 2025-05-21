package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Exceptions.DatoNoValido;
import com.example.proyectgame.Exceptions.VideojuegoYaExisteException;
import com.example.proyectgame.Model.Genero;
import com.example.proyectgame.Model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDAO implements DAOinterface<Videojuego> {

    private static final String SQL_INSERT = "INSERT INTO videojuego (titulo, descripcion, desarrolladora, genero, fechalanzamiento, portada) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE videojuego SET titulo = ?, descripcion = ?, desarrolladora = ?, genero = ?, fechalanzamiento = ?, portada = ? WHERE id = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM videojuego WHERE titulo = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM videojuego WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM videojuego WHERE id = ?";
    private final static String SQL_FIND_BY_GENERO = "SELECT * FROM videojuego WHERE genero = ?";


    public boolean insert(Videojuego videojuego) {
        boolean added = false;
        if (videojuego != null && findByName(videojuego.getTitulo()) != null) {
            throw new VideojuegoYaExisteException("Ya existe un videojuego con este nombre.");
        }

        if (videojuego.getTitulo() == null || videojuego.getDescripcion() == null || videojuego.getGenero() == null ||  videojuego.getDesarrolladora() == null || videojuego.getPortada() == null) {
            throw new DatoNoValido("Se deben rellenar todos los campos.");
        }

        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, videojuego.getTitulo());
            ps.setString(2, videojuego.getDescripcion());
            ps.setString(3, videojuego.getDesarrolladora());
            ps.setString(4, videojuego.getGenero().name());
            ps.setDate(5, Date.valueOf(videojuego.getFechalanzamiento()));
            ps.setString(6, videojuego.getPortada());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                added = true;
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    videojuego.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return added;
    }

    public boolean delete(int id) {
        boolean deleted = false;
        if (findById(id)!=null) {
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
        }
        return deleted;
    }

    public Videojuego findByName(String titulo) {
        Videojuego videojuego = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_NAME);
            ps.setString(1, titulo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                videojuego = new Videojuego();
                videojuego.setId(rs.getInt("id"));
                videojuego.setTitulo(rs.getString("titulo"));
                videojuego.setDescripcion(rs.getString("descripcion"));
                videojuego.setDesarrolladora(rs.getString("desarrolladora"));
                videojuego.setGenero(Genero.valueOf(rs.getString("genero")));
                videojuego.setFechalanzamiento(rs.getDate("fechalanzamiento").toLocalDate());
                videojuego.setPortada(rs.getString("portada"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return videojuego;
    }

    /**
     * Actualiza los datos de un usuario
     * @param videojuegoNuevo objeto usuario con los datos nuevos
     * @param  videojuegoActual usuario que se va cambiar
     * @return true si se actualiza correctamente
     */
    public boolean update(Videojuego videojuegoNuevo, Videojuego videojuegoActual) {
        boolean updated = false;
        if (videojuegoNuevo == null && findByName(videojuegoActual.getTitulo()) != null) {
            throw new VideojuegoYaExisteException("Ya existe un videojuego con este nombre.");
        }
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_UPDATE);
            ps.setString(1, videojuegoNuevo.getTitulo());
            ps.setString(2, videojuegoNuevo.getDescripcion());
            ps.setString(3, videojuegoNuevo.getDesarrolladora());
            ps.setString(4, videojuegoNuevo.getGenero().name());
            ps.setDate(5, Date.valueOf(videojuegoNuevo.getFechalanzamiento()));
            ps.setString(6, videojuegoNuevo.getPortada());
            ps.setInt(7, videojuegoActual.getId());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                updated = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updated;
    }

    public Videojuego findById(int id) {
        Videojuego videojuego = null;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                videojuego = new Videojuego();
                videojuego.setId(rs.getInt("id"));
                videojuego.setTitulo(rs.getString("titulo"));
                videojuego.setDescripcion(rs.getString("descripcion"));
                videojuego.setDesarrolladora(rs.getString("desarrolladora"));
                videojuego.setGenero(Genero.valueOf(rs.getString("genero")));
                videojuego.setFechalanzamiento(rs.getDate("fechalanzamiento").toLocalDate());
                videojuego.setPortada(rs.getString("portada"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return videojuego;
    }

    public List<Videojuego> findAll() {
        List<Videojuego> lista = new ArrayList<>();
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM videojuego");
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                Videojuego videojuego = new Videojuego();
                videojuego.setId(rs.getInt("id"));
                videojuego.setTitulo(rs.getString("titulo"));
                videojuego.setDescripcion(rs.getString("descripcion"));
                videojuego.setDesarrolladora(rs.getString("desarrolladora"));
                videojuego.setGenero(Genero.valueOf(rs.getString("genero")));
                videojuego.setFechalanzamiento(rs.getDate("fechalanzamiento").toLocalDate());
                videojuego.setPortada(rs.getString("portada"));
                lista.add(videojuego);
            }

            ResenaDAO resenaDAO = new ResenaDAO();
            for (Videojuego v : lista) {
                v.setResenaList(resenaDAO.findResenasByVideojuego(v));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    public List<Videojuego> findByGenero(Genero genero) {
        List<Videojuego> lista = new ArrayList<>();

        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_BY_GENERO);
            ps.setString(1, genero.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Videojuego videojuego = new Videojuego();
                videojuego.setId(rs.getInt("id"));
                videojuego.setTitulo(rs.getString("titulo"));
                videojuego.setDescripcion(rs.getString("descripcion"));
                videojuego.setDesarrolladora(rs.getString("desarrolladora"));
                videojuego.setGenero(Genero.valueOf(rs.getString("genero")));
                videojuego.setFechalanzamiento(rs.getDate("fechalanzamiento").toLocalDate());
                videojuego.setPortada(rs.getString("portada"));
                lista.add(videojuego);
            }

            ResenaDAO resenaDAO = new ResenaDAO();
            for (Videojuego v : lista) {
                v.setResenaList(resenaDAO.findResenasByVideojuego(v));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}

