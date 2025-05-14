package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public class VidejuegosViewController {

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private GridPane gridPainVideojuegos;

    @FXML
    public void initialize() {
        // Llamamos al método cargarVideojuegos al inicializar el controlador
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

        gridPainVideojuegos.setVgap(30);
        gridPainVideojuegos.setHgap(20);

        for (Videojuego videojuego : videojuegos) {
            // Crear el VBox para cada videojuego
            VBox vbox = crearCeldaJuego(videojuego);

            // Añadir el VBox al GridPane
            gridPainVideojuegos.add(vbox, column, row);

            // Aumenta la columna, y si supera el límite de columnas, resetea la columna y aumenta la fila
            column++;
            if (column > 2) {  // Asumiendo que el Grid tiene 3 columnas
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

        return vbox;
    }

    public void addVideojuego(ActionEvent actionEvent) {

    }
}