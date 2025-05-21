package com.example.proyectgame.DAO;

import com.example.proyectgame.DataBase.ConnectionBD;
import com.example.proyectgame.Exceptions.ResenaYaExisteException;
import com.example.proyectgame.Model.Resena;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResenaDAO implements DAOinterface <Resena>{
    private final static String SQL_INSERT = "INSERT INTO resena (comentario, puntuacion, usuarioid, videojuegoid) VALUES (?, ?, ?, ?)";
    private final static String SQL_DELETE_BY_ID = "DELETE FROM resena WHERE id = ?";
    private final static String SQL_UPDATE = "UPDATE resena SET comentario = ?, puntuacion = ?, usuarioid = ?, videojuegoid = ? WHERE id = ?";
    private final static String SQL_FIND_BY_ID = "SELECT * FROM resena WHERE id = ?";
    private final static String SQL_FIND_BY_VIDEOJUEGO = "SELECT * FROM resena WHERE videojuegoid = ?";
    private final static String SQL_FIND_RESENA_VIDEOJUEGO_USUARIO = "SELECT COUNT(*) FROM resena WHERE usuarioId = ? AND videojuegoId = ?";
    private final static String SQL_FIND_BY_USUARIO = "SELECT * FROM resena WHERE usuarioid = ?";


    /**
     * Inserta una nueva reseña en la base de datos.
     * Si el usuario ya ha reseñado el videojuego, lanza una excepción.
     * @param resena la reseña que se desea insertar
     * @return true si la reseña fue insertada correctamente, false en caso contrario
     * @throws ResenaYaExisteException si ya existe una reseña del mismo usuario para el videojuego
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     */
    public  boolean insert(Resena resena) {
        boolean added = false;
        if (existeResena(resena.getUsuario().getId(), resena.getVideojuego().getId())) {
            throw new ResenaYaExisteException("El usuario ya ha reseñado este videojuego.");
        }

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
                    resena.setId(rs.getInt(1));
                }
                added = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return added;
    }

    /**
     * Elimina una reseña de la base de datos por su ID.
     * @param id el ID de la reseña a eliminar
     * @return true si la reseña fue eliminada correctamente, false en caso contrario
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     */
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

    /**
     * Actualiza una reseña existente con nuevos datos.
     * @param resenaNueva la nueva información de la reseña
     * @param resenaActual la reseña actual que se va a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     */
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

    /**
     * Recupera una reseña con todos sus datos relacionados (usuario y videojuego) según su ID.
     * @param id el ID de la reseña a buscar
     * @return la reseña encontrada o null si no existe
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     */
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

    /**
     * Recupera una reseña por su ID sin cargar los datos de usuario ni videojuego. Se usa para comprobaciones.
     * @param id el ID de la reseña
     * @return la reseña encontrada sin relaciones o null si no existe
     * @throws RuntimeException si ocurre un error al acceder a la base de datos
     */
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

    /**
     * Obtiene todas las reseñas asociadas a un videojuego específico.
     * @param videojuego el videojuego del cual obtener las reseñas
     * @return una lista de reseñas correspondientes al videojuego
     */
    public List<Resena> findResenasByVideojuego(Videojuego videojuego) {
        List<Resena> resenas = new ArrayList<>();

        try (Connection conn = ConnectionBD.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_VIDEOJUEGO);
            stmt.setInt(1, videojuego.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                Usuario usuario = usuarioDAO.findById(rs.getInt("Usuarioid"));
                Resena resena = new Resena(rs.getString("comentario"), rs.getInt("puntuacion"), videojuego, usuario);

                resena.setId(rs.getInt("id"));
                resena.setFechaPublicacion(rs.getDate("fecha").toLocalDate());
                resenas.add(resena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resenas;
    }

    /**
     * Verifica si un usuario ya ha realizado una reseña sobre un videojuego.
     * @param usuarioId ID del usuario
     * @param videojuegoId ID del videojuego
     * @return true si la reseña existe, false si no
     */
    public boolean existeResena(int usuarioId, int videojuegoId) {

        boolean existe = false;
        try {
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_FIND_RESENA_VIDEOJUEGO_USUARIO);
            ps.setInt(1, usuarioId);
            ps.setInt(2, videojuegoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                existe = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existe;
    }

    /**
     * Recupera todas las reseñas realizadas por un usuario específico.
     * @param id ID del usuario
     * @return lista de reseñas hechas por el usuario
     */
    public List<Resena> findByUsuario(int id) {
        List<Resena> resenas = new ArrayList<>();

        try {
            Connection conn = ConnectionBD.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SQL_FIND_BY_USUARIO);
            stmt.setInt(1, id); // usamos el ID del objeto Usuario
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                Resena resena = new Resena();
                VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
                UsuarioDAO usuarioDAO = new UsuarioDAO();

                resena.setId(rs.getInt("id"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setComentario(rs.getString("comentario"));
                resena.setVideojuego(videojuegoDAO.findById(rs.getInt("videojuegoId")));
                resena.setUsuario(usuarioDAO.findById(rs.getInt("usuarioId")));
                if (rs.getDate("fecha") != null) {
                    resena.setFechaPublicacion(rs.getDate("fecha").toLocalDate());
                }
                resenas.add(resena);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resenas;
    }
}
