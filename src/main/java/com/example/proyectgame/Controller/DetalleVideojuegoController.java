package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.ResenaDAO;
import com.example.proyectgame.Model.Resena;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;
import com.example.proyectgame.Utilities.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

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


    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        mostrarDetalles();
    }

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
            double media = videojuego.getResenaList().stream().mapToInt(Resena::getPuntuacion).average().orElse(0);
            puntuacionMediaLabel.setText("Puntuación media: " + String.format("%.1f", media));
        } else {
            puntuacionMediaLabel.setText("Puntuación media: N/A");
        }

        // Mostrar reseñas
        resenasBox.getChildren().clear();
        ResenaDAO resenaDAO = new ResenaDAO();
        List<Resena> resenas = resenaDAO.findResenasByVideojuego(videojuego);

        if (resenas != null) {
            for (Resena resena : resenas) {
                Label label = new Label("⭐ " + resena.getPuntuacion() + " - " + resena.getComentario());
                label.setWrapText(true);
                resenasBox.getChildren().add(label);
            }
        }
    }

    public void addResena(ActionEvent actionEvent) {
        //añadir excepcion si el usuario ya ha reseñado el videojuego
        String comentarioResena = this.comentarioResena.getText();
        int puntuacion = (int) sliderPuntuacion.getValue();
        Videojuego videojuego = this.videojuego;
        Usuario usuario = Sesion.getInstancia().getUsuarioIniciado();

        ResenaDAO dao = new ResenaDAO();
        Resena resena = new Resena(comentarioResena, puntuacion, videojuego, usuario);
        dao.insert(resena);

        this.comentarioResena.clear();
        this.sliderPuntuacion.setValue(3);
        mostrarDetalles(); //para recargar
    }
}
