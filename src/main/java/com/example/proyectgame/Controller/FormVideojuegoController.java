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
    @FXML private DatePicker fechaLanzamiento;
    @FXML private TextField desarrolladorField;
    @FXML private TextField portadaField;
    @FXML private Button guardarButton;

    @FXML private Videojuego videojuegoSeleccionado;
    private boolean esEdicion = false;

    @FXML
    private void initialize() {
        guardarButton.setOnAction(event -> {
            if (esEdicion) {
                updateVideojuego();
            } else {
                guardarVideojuego();
            }
        });
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuegoSeleccionado = videojuego;
        this.esEdicion = true;

        // Rellenar campos del formulario con los datos del videojuego
        tituloField.setText(videojuego.getTitulo());
        descripcionArea.setText(videojuego.getDescripcion());
        generoField.setText(videojuego.getGenero());
        fechaLanzamiento.setValue(videojuego.getFechalanzamiento());
        desarrolladorField.setText(videojuego.getDesarrolladora());
        portadaField.setText(videojuego.getPortada());
    }


    private void guardarVideojuego() {
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        String genero = generoField.getText();
        LocalDate fecha = fechaLanzamiento.getValue();
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

    public void updateVideojuego(){
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        String genero = generoField.getText();
        LocalDate fecha = fechaLanzamiento.getValue();
        String desarrollador = desarrolladorField.getText();
        String portada = portadaField.getText();


        Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, genero, fecha, portada);
        VideojuegoDAO dao = new VideojuegoDAO();
        boolean actualizado = dao.update(nuevo, videojuegoSeleccionado);

        if (actualizado) {
            ((Stage) guardarButton.getScene().getWindow()).close(); // Cierra la ventana
        } else {
            //excepciones o errores pendiente
        }
    }
}
