package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VidejuegosViewController {

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private GridPane gridPaneVideojuegos;
    @FXML
    private VBox celdaSeleccionada; // Guarda la VBox actualmente seleccionada
    @FXML
    private Videojuego videojuegoSeleccionado; // Guarda el videojuego correspondiente
    @FXML
    public void initialize() {
        cargarVideojuegos();
    }

    public void setVisibilidad(Usuario usuarioLogeado) {
        if (usuarioLogeado.getRolUsuario() != RolUsuario.ADMINISTRADOR) {
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    public void cargarVideojuegos() {
        // Obtener la lista de videojuegos de la BD
        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
        List<Videojuego> videojuegos = videojuegoDAO.findAll();

        int row = 0;
        int column = 0;

        for (Videojuego videojuego : videojuegos) {
            // Crear el VBox para cada videojuego
            VBox vbox = crearCeldaJuego(videojuego);

            // Añadir el VBox al GridPane
            gridPaneVideojuegos.add(vbox, column, row);

            // Aumenta la columna, y si supera el límite de columnas, resetea la columna y aumenta la fila
            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }

    private VBox crearCeldaJuego(Videojuego juego) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);


        // Carga de la imagen desde recursos
        String rutaImagen = juego.getPortadaUrl();
        Image imagen = new Image(getClass().getResourceAsStream(rutaImagen), 120, 160, true, true);
        ImageView imageView = new ImageView(imagen);

        // Nombre del videojuego
        Text nombre = new Text(juego.getTitulo());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Botón para ver detalles (puedes agregar su lógica más adelante)
        Button button = new Button("Ver detalles");

        // Agregar imagen, nombre y botón al VBox
        vbox.getChildren().addAll(imageView, nombre, button);
        vbox.setStyle("-fx-border-color: #ccc; -fx-background-color: #f4f4f4; -fx-padding: 0;");

        //Capacidad de seleccionar celdas
        vbox.setOnMouseClicked(event -> {
            if (celdaSeleccionada != null) {
                celdaSeleccionada.setStyle("-fx-border-color: #ccc; -fx-background-color: #f4f4f4; -fx-padding: 10;");
            }

            celdaSeleccionada = vbox;
            videojuegoSeleccionado = juego;
            celdaSeleccionada.setStyle("-fx-border-color: #0078D7; -fx-background-color: #D0E8FF; -fx-padding: 10;");
        });

        return vbox;
    }

    public void addVideojuego(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/formVideojuego.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Añadir nuevo videojuego");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Después de cerrarse, recargar videojuegos en la vista principal
            cargarVideojuegos();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteVideojuego(ActionEvent actionEvent) {
        if (videojuegoSeleccionado != null) {
            VideojuegoDAO dao = new VideojuegoDAO();
            dao.delete(videojuegoSeleccionado.getId());
            videojuegoSeleccionado = null;
            celdaSeleccionada = null;
            gridPaneVideojuegos.getChildren().clear(); //limpiar grid
            cargarVideojuegos();
        } else {
            System.out.println("No hay ningún videojuego seleccionado.");
        }
    }

    public void updateVideojuego(ActionEvent actionEvent) {
        if (videojuegoSeleccionado != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/formVideojuego.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setTitle("Editar videojuego");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            VideojuegoDAO dao = new VideojuegoDAO();

            gridPaneVideojuegos.getChildren().clear(); //limpiar grid
            cargarVideojuegos();
        } else {
            System.out.println("No hay ningún videojuego seleccionado.");
        }
    }
}