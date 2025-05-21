package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.ResenaDAO;
import com.example.proyectgame.Exceptions.ResenaYaExisteException;
import com.example.proyectgame.Model.Resena;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;
import com.example.proyectgame.Utilities.Sesion;
import com.example.proyectgame.Utilities.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class DetalleVideojuegoController {

    @FXML private ImageView portadaImageView;
    @FXML private Label tituloLabel;
    @FXML private Label generoLabel;
    @FXML private Label desarrolladorLabel;
    @FXML private Label fechaLabel;
    @FXML private Label descripcionLabel;
    @FXML private Label puntuacionMediaLabel;
    @FXML private VBox resenasBox;
    @FXML private TextArea comentarioResena;
    @FXML private Slider sliderPuntuacion;
    private Videojuego videojuego;

    /**
     * Establece el videojuego actual y muestra sus detalles en la vista.
     * @param videojuego el videojuego cuyos detalles se van a mostrar.
     * Este metodo se usa antes de lanzar la escena.
     */
    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        mostrarDetalles();
    }

    /**
     * Muestra todos los detalles del videojuego en la interfaz:
     * También obtiene las reseñas desde la base de datos usando ResenaDAO.
     */
    private void mostrarDetalles() {
        if (videojuego == null) return;

        tituloLabel.setText(videojuego.getTitulo());
        generoLabel.setText("Género: " + videojuego.getGenero());
        desarrolladorLabel.setText("Desarrollador: " + videojuego.getDesarrolladora());
        fechaLabel.setText("Fecha de lanzamiento: " + (videojuego.getFechalanzamiento() != null ? videojuego.getFechalanzamiento().toString() : ""));
        descripcionLabel.setText(videojuego.getDescripcion());

        try {
            String rutaImagen = videojuego.getPortadaUrl();
            Image imagen = new Image(getClass().getResourceAsStream(rutaImagen), 120, 160, true, true);
            portadaImageView.setImage(imagen);
        } catch (Exception e) {
            portadaImageView.setImage(null); // o una imagen por defecto
        }

        // Mostrar puntuación media
        if (videojuego.getResenaList() != null && !videojuego.getResenaList().isEmpty()) {
            double media = videojuego.getResenaList().stream().mapToInt(resena -> resena.getPuntuacion()).average().orElse(0);
            puntuacionMediaLabel.setText("Puntuación media: " + String.format("%.1f", media));
        } else {
            puntuacionMediaLabel.setText("Puntuación media: N/A");
        }

        // Mostrar reseñas
        resenasBox.getChildren().clear();
        ResenaDAO resenaDAO = new ResenaDAO();
        videojuego.setResenaList(resenaDAO.findResenasByVideojuego(videojuego));


        if (videojuego.getResenaList() != null) {
            for (Resena resena : videojuego.getResenaList()) {
                Label label = new Label("⭐ " + resena.getPuntuacion() + " - " + resena.getComentario());
                label.setWrapText(true);
                resenasBox.getChildren().add(label);
            }
        }
    }

    /**
     * Añade una nueva reseña al videojuego actual.
     * La guarda usando el ResenaDAO.
     * Añade la reseña a la lista del videojuego si se inserta correctamente.
     * Recarga los detalles para mostrar la nueva reseña.
     * Muestra una alerta si ya existe una reseña del usuario para este juego.
     * @param actionEvent el evento del botón pulsado
     */
    public void addResena(ActionEvent actionEvent) {
        String comentarioResena = this.comentarioResena.getText();
        int puntuacion = (int) sliderPuntuacion.getValue();
        Videojuego videojuego = this.videojuego;
        Usuario usuario = Sesion.getInstancia().getUsuarioIniciado();

        ResenaDAO dao = new ResenaDAO();
        Resena resena = new Resena(comentarioResena, puntuacion, videojuego, usuario);
        try {
            dao.insert(resena);
            videojuego.getResenaList().add(resena);
        }catch (ResenaYaExisteException e){
            Utilidades.mostrarAlerta("Error", e.getMessage());
        }

        this.comentarioResena.clear();
        this.sliderPuntuacion.setValue(3);
        mostrarDetalles(); //para recargar
    }

    /**
     * Vuelve a la vista anterior (VideojuegosView.fxml).
     * @param actionEvent el evento del botón "Volver"
     */
    public void volverVentanaAnterior(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/VideojuegosView.fxml"));
            Parent root = loader.load();

            // Obtener el Stage actual desde el botón que disparó el evento
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
