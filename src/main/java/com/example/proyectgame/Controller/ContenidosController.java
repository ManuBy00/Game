package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.GuiaDAO;
import com.example.proyectgame.DAO.NoticiaDAO;
import com.example.proyectgame.Model.Contenido;
import com.example.proyectgame.Model.Guia;
import com.example.proyectgame.Model.Noticia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ContenidosController {

    @FXML
    private ListView<Contenido> contenidosList;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label subtituloLabel;

    @FXML
    private Label cuerpoLabel;

    @FXML
    private Label autorLabel;

    @FXML
    private Label fechaLabel;

    NoticiaDAO noticiaDAO = new NoticiaDAO();
    GuiaDAO guiaDAO = new GuiaDAO();

    private Contenido contenidoSeleccionado;

    @FXML
    public void initialize() {
        contenidosList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Contenido item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitulo());
                }
            }
        });
        cargarContenidos();
    }

    private void cargarContenidos() {
        List<Noticia> noticias = noticiaDAO.findAll();
        List<Guia> guias = guiaDAO.findAll();

        List<Contenido> contenidos = new ArrayList<>();
        contenidos.addAll(noticias);
        contenidos.addAll(guias);

        contenidosList.getItems().setAll(contenidos);
    }

    @FXML
    private void mostrarContenidoSeleccionado(MouseEvent event) {
        contenidoSeleccionado = contenidosList.getSelectionModel().getSelectedItem();
        if (contenidoSeleccionado != null) {
            mostrarContenido(contenidoSeleccionado);
        }
    }

    private void mostrarContenido(Contenido contenido) {
        tituloLabel.setText(contenido.getTitulo());

        if (contenido instanceof Noticia noticia) {
            subtituloLabel.setText(noticia.getSubtitulo());
            cuerpoLabel.setText(noticia.getCuerpo());
        } else if (contenido instanceof Guia guia) {
            subtituloLabel.setText("Guía de: " + guia.getVideojuego().getTitulo());
            cuerpoLabel.setText(guia.getCuerpo());
        } else {
            subtituloLabel.setText("");
            cuerpoLabel.setText("");
        }

        autorLabel.setText("Autor: " + (contenido.getAutor() != null ? contenido.getAutor().getNombre() : "Desconocido"));

        if (contenido.getFechaPublicacion() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaLabel.setText("Fecha: " + contenido.getFechaPublicacion().format(formatter));
        } else {
            fechaLabel.setText("Fecha: Desconocida");
        }
    }

    private void abrirFormulario(Contenido contenido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/ContenidoForm.fxml"));
            Parent root = loader.load();

            ContenidoFormController controller = loader.getController();
            if (contenido != null) {
                controller.setContenido(contenido);
            }

            Stage stage = new Stage();
            stage.setTitle(contenido == null ? "Insertar Contenido" : "Editar Contenido");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // BLOQUEA hasta cerrar
            stage.showAndWait(); // ESPERA a que se cierre


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void lanzarInsertForm() {
        abrirFormulario(null);
        cargarContenidos();
    }

    @FXML
    private void lanzarUpdateForm() {
        if (contenidoSeleccionado == null) {
            mostrarAlerta("Selecciona un contenido para editar.");
            return;
        }
        abrirFormulario(contenidoSeleccionado);
        cargarContenidos();
    }

    @FXML
    private void DeleteContenido() {
        if (contenidoSeleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar borrado");
            confirm.setHeaderText("¿Seguro que quieres eliminar este contenido?");
            confirm.setContentText(contenidoSeleccionado.getTitulo());

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (contenidoSeleccionado instanceof Noticia) {
                        noticiaDAO.delete(contenidoSeleccionado.getId());
                    }else if (contenidoSeleccionado instanceof Guia) {
                        guiaDAO.delete(contenidoSeleccionado.getId());
                    }
                    contenidosList.getItems().remove(contenidoSeleccionado);
                    limpiarVista();
                    cargarContenidos();
                }
            });
        } else {
            mostrarAlerta("Selecciona un contenido para borrar.");
        }
    }

    private void limpiarVista() {
        tituloLabel.setText("TITULO");
        subtituloLabel.setText("SUBTITULO");
        cuerpoLabel.setText("Cuerpo");
        autorLabel.setText("Autor");
        fechaLabel.setText("Fecha label");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    public void lanzarVistaUsuario(ActionEvent actionEvent) {
    }

    public void lanzarVistaVideojuegos(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/VideojuegosView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Videojuegos");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarVistaContenido(ActionEvent actionEvent) {
        cargarContenidos();
    }
}