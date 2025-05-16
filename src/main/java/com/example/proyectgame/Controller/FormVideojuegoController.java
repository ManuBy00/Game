package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.Videojuego;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FormVideojuegoController {

    @FXML
    private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private TextField generoField;
    @FXML private DatePicker fechaPicker;
    @FXML private TextField desarrolladorField;
    @FXML private TextField portadaField;
    @FXML private Button guardarButton;

    @FXML
    private void initialize() {
        guardarButton.setOnAction(event -> guardarVideojuego());
    }

    private void guardarVideojuego() {
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        String genero = generoField.getText();
        LocalDate fecha = fechaPicker.getValue();
        String desarrollador = desarrolladorField.getText();
        String portada = portadaField.getText();

        Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, genero, fecha, portada);
        VideojuegoDAO dao = new VideojuegoDAO();
        boolean insertado = dao.insert(nuevo);

        if (insertado) {
            ((Stage) guardarButton.getScene().getWindow()).close(); // Cierra la ventana
        } else {
            //excepciones o errores pendiente
        }
    }
}