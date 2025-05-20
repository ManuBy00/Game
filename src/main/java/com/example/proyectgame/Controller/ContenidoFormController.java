package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.GuiaDAO;
import com.example.proyectgame.DAO.NoticiaDAO;
import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.*;
import com.example.proyectgame.Utilities.Sesion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ContenidoFormController {

    @FXML private TextField tituloField;
    @FXML private TextArea cuerpoArea;
    @FXML private DatePicker fechaPicker;
    @FXML private ComboBox<String> tipoComboBox;
    @FXML private Label campoExtraLabel;
    @FXML private TextField campoExtraField; // Para subtítulo de Noticia
    @FXML private ComboBox<Videojuego> campoExtraComboBox; // Para videojuego en Guía

    private final NoticiaDAO noticiaDAO = new NoticiaDAO();
    private final GuiaDAO guiaDAO = new GuiaDAO();
    private final VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    private Contenido contenidoExistente;

    @FXML
    public void initialize() {
        tipoComboBox.getItems().addAll("Noticia", "Guia");
        tipoComboBox.setOnAction(e -> actualizarCampoExtra());

        campoExtraField.setVisible(false);
        campoExtraComboBox.setVisible(false);
        campoExtraLabel.setVisible(false);

        campoExtraComboBox.setItems(FXCollections.observableArrayList(videojuegoDAO.findAll()));

        campoExtraComboBox.setCellFactory(lv -> new ListCell<Videojuego>() {
            @Override
            protected void updateItem(Videojuego item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitulo());
            }
        });
    }

    public void setContenido(Contenido contenido) {
        this.contenidoExistente = contenido;

        tituloField.setText(contenido.getTitulo());
        cuerpoArea.setText(contenido.getCuerpo());
        fechaPicker.setValue(contenido.getFechaPublicacion());

        if (contenido instanceof Noticia) {
            tipoComboBox.setValue("Noticia");
            campoExtraField.setText(((Noticia) contenido).getSubtitulo());
        } else if (contenido instanceof Guia) {
            tipoComboBox.setValue("Guia");
            campoExtraComboBox.setValue(((Guia) contenido).getVideojuego());
        }

        actualizarCampoExtra();
    }

    private void actualizarCampoExtra() {
        String tipo = tipoComboBox.getValue();

        campoExtraField.setVisible(false);
        campoExtraComboBox.setVisible(false);
        campoExtraLabel.setVisible(false);

        if ("Noticia".equals(tipo)) {
            campoExtraLabel.setText("Subtítulo");
            campoExtraLabel.setVisible(true);
            campoExtraField.setVisible(true);
        } else if ("Guia".equals(tipo)) {
            campoExtraLabel.setText("Videojuego relacionado");
            campoExtraLabel.setVisible(true);
            campoExtraComboBox.setVisible(true);
        }
    }

    @FXML
    private void guardarContenido(ActionEvent event) {
        String titulo = tituloField.getText();
        String cuerpo = cuerpoArea.getText();
        LocalDate fecha = fechaPicker.getValue();
        String tipo = tipoComboBox.getValue();

        if (titulo.isEmpty() || cuerpo.isEmpty() || fecha == null || tipo == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }


        Usuario autor = Sesion.getInstancia().getUsuarioIniciado();


        if (contenidoExistente == null) {
            // Crear nuevo
            if ("Noticia".equals(tipo)) {
                String subtitulo = campoExtraField.getText();
                if (subtitulo.isEmpty()) {
                    mostrarAlerta("El subtítulo no puede estar vacío.");
                    return;
                }
                Noticia noticia = new Noticia(titulo, fecha, cuerpo, autor, subtitulo);
                noticiaDAO.insert(noticia);
            } else if ("Guia".equals(tipo)) {
                Videojuego videojuego = campoExtraComboBox.getValue();
                if (videojuego == null) {
                    mostrarAlerta("Debes seleccionar un videojuego.");
                    return;
                }
                Guia guia = new Guia(titulo, fecha, autor, cuerpo, videojuego);

                guiaDAO.insert(guia);
            }
        } else {
            // Actualizar existente
            contenidoExistente.setTitulo(titulo);
            contenidoExistente.setCuerpo(cuerpo);
            contenidoExistente.setFechaPublicacion(fecha);

            if (contenidoExistente instanceof Noticia) {
                Noticia noticiaActual = (Noticia) contenidoExistente;
                String subtitulo = campoExtraField.getText();
                if (subtitulo.isEmpty()) {
                    mostrarAlerta("El subtítulo no puede estar vacío.");
                    return;
                }
                Noticia noticiaNueva = new Noticia(titulo, fecha, cuerpo, autor, subtitulo);
                noticiaDAO.update(noticiaNueva, noticiaActual);
            } else if (contenidoExistente instanceof Guia) {
                Guia guiaActual = (Guia) contenidoExistente;
                Videojuego videojuego = campoExtraComboBox.getValue();
                if (videojuego == null) {
                    mostrarAlerta("Debes seleccionar un videojuego.");
                    return;
                }
                Guia guiaNueva = new Guia(titulo, fecha, autor, cuerpo, videojuego);
                guiaDAO.update(guiaNueva, guiaActual);
            }
        }

        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tituloField.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Formulario incompleto");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

