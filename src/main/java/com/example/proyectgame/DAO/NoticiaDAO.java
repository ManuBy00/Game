package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Exceptions.NoticiaYaExisteException;
import com.example.proyectgame.Model.Noticia;
import com.example.proyectgame.Model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticiaDAO implements DAOinterface<Noticia>{

    private static final String SQL_INSERT = "INSERT INTO noticia (titulo, subtitulo, fechaPublicacion, autorId, cuerpo) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE noticia SET titulo = ?, subtitulo = ?, fechaPublicacion = ?, autorId = ?, cuerpo = ? WHERE id = ?";

    private static final String SQL_DELETE_BY_ID = "DELETE FROM noticia WHERE id = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM noticia";

    private static final String SQL_FIND_BY_TITULO = "SELECT * FROM noticia WHERE titulo = ?";

    private static final String SQL_FIND_BY_ID = "SELECT * FROM noticia WHERE id = ?";

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Inserta una nueva noticia en la base de datos.
     * @param noticia el objeto Noticia a insertar
     * @return true si la noticia se ha insertado correctamente, false en caso contrario
     * @throws NoticiaYaExisteException si ya existe una noticia con el mismo título
     */
    public boolean insert(Noticia noticia) {
        boolean added = false;
        if (findByTitulo(noticia.getTitulo()) != null) {
            throw new NoticiaYaExisteException("Ya existe una noticia con ese título.");
        }
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, noticia.getTitulo());
            ps.setString(2, noticia.getSubtitulo());
            ps.setDate(3, Date.valueOf(noticia.getFechaPublicacion()));
            ps.setInt(4, noticia.getAutor().getId());
            ps.setString(5, noticia.getCuerpo());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    noticia.setId(idGenerado);
                }
                added = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return added;
    }


    /**
     * Elimina una noticia de la base de datos por su ID.
     * @param id el ID de la noticia a eliminar
     * @return true si la noticia fue eliminada correctamente, false si no se encontró la noticia
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
     * Actualiza una noticia existente en la base de datos.
     * @param noticiaNueva el objeto Noticia con los datos actualizados
     * @param noticiaActual el objeto Noticia original que se desea actualizar
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean update(Noticia noticiaNueva, Noticia noticiaActual) {
        boolean result = false;
        if (noticiaNueva != null) {
            try {
                PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(SQL_UPDATE);
                pst.setString(1, noticiaNueva.getTitulo());
                pst.setString(2, noticiaNueva.getSubtitulo());
                pst.setDate(3, Date.valueOf(noticiaNueva.getFechaPublicacion()));
                pst.setInt(4, noticiaNueva.getAutor().getId());
                pst.setString(5, noticiaNueva.getCuerpo());
                pst.setInt(6, noticiaActual.getId());

                int filas = pst.executeUpdate();
                result = filas > 0;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Obtiene todas las noticias almacenadas en la base de datos.
     *
     * @return una lista con todas las noticias
     */
    public List<Noticia> findAll() {
        List<Noticia> lista = new ArrayList<>();
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Noticia noticia = new Noticia();
                noticia.setId(rs.getInt("id"));
                noticia.setTitulo(rs.getString("titulo"));
                noticia.setSubtitulo(rs.getString("subtitulo"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                noticia.setCuerpo(rs.getString("cuerpo"));

                int autorId = rs.getInt("autorId");
                Usuario autor = usuarioDAO.findById(autorId);
                noticia.setAutor(autor);

                lista.add(noticia);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    /**
     * Busca una noticia por su título.
     * @param titulo el título de la noticia a buscar
     * @return el objeto Noticia correspondiente si se encuentra, null si no existe
     */
    public Noticia findByTitulo(String titulo) {
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
                noticia.setSubtitulo(rs.getString("subtitulo"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                noticia.setCuerpo(rs.getString("cuerpo"));

                int autorId = rs.getInt("autorId");
                Usuario autor = usuarioDAO.findById(autorId);
                noticia.setAutor(autor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noticia;
    }

    /**
     * Busca una noticia por su ID.
     * @param id el ID de la noticia a buscar
     * @return el objeto Noticia correspondiente si se encuentra, null si no existe
     */
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
                noticia.setSubtitulo(rs.getString("subtitulo"));
                noticia.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                noticia.setCuerpo(rs.getString("cuerpo"));

                int autorId = rs.getInt("autorId");
                Usuario autor = usuarioDAO.findById(autorId);
                noticia.setAutor(autor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return noticia;
    }
}

