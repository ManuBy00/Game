package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Exceptions.DatoNoValido;
import com.example.proyectgame.Exceptions.VideojuegoYaExisteException;
import com.example.proyectgame.Model.Genero;
import com.example.proyectgame.Model.Videojuego;
import com.example.proyectgame.Utilities.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FormVideojuegoController {

    @FXML
    private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private ComboBox<Genero> generoComboBox;
    @FXML private DatePicker fechaLanzamiento;
    @FXML private TextField desarrolladorField;
    @FXML private TextField portadaField;
    @FXML private Button guardarButton;
    @FXML private Videojuego videojuegoSeleccionado;

    private boolean esEdicion = false;

    @FXML
    private void initialize() {
        generoComboBox.getItems().setAll(Genero.values());

    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuegoSeleccionado = videojuego;
        this.esEdicion = true;

        // Rellenar campos del formulario con los datos del videojuego
        tituloField.setText(videojuego.getTitulo());
        descripcionArea.setText(videojuego.getDescripcion());
        generoComboBox.setValue(videojuego.getGenero());
        fechaLanzamiento.setValue(videojuego.getFechalanzamiento());
        desarrolladorField.setText(videojuego.getDesarrolladora());
        portadaField.setText(videojuego.getPortada());
    }

    @FXML
    public void guardarButton(ActionEvent actionEvent) {
        if (esEdicion) {
            try{
                updateVideojuego();
            }catch (VideojuegoYaExisteException e){
                Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
            }
        } else {
            try {
                addVideojuego();
            }catch (VideojuegoYaExisteException e){
                Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
            }
        }
    }


    private void addVideojuego() {
        try {
            String titulo = tituloField.getText();
            String descripcion = descripcionArea.getText();
            Genero generoEnum = generoComboBox.getValue();
            LocalDate fecha = Utilidades.validarFecha(fechaLanzamiento.getValue());
            String desarrollador = desarrolladorField.getText();
            String portada = portadaField.getText();

            Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, generoEnum, fecha, portada);
            VideojuegoDAO dao = new VideojuegoDAO();

            dao.insert(nuevo);
            ((Stage) guardarButton.getScene().getWindow()).close(); // Cierra la ventana
        }catch (VideojuegoYaExisteException e){
            Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
        }catch (DatoNoValido e){
            Utilidades.mostrarAlerta("Error", e.getMessage());
        }
    }

    public void updateVideojuego(){
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        Genero genero = generoComboBox.getValue();
        LocalDate fecha = fechaLanzamiento.getValue();
        String desarrollador = desarrolladorField.getText();
        String portada = portadaField.getText();

        Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, genero, fecha, portada);
        VideojuegoDAO dao = new VideojuegoDAO();

        try{
            dao.update(nuevo, videojuegoSeleccionado);
            ((Stage) guardarButton.getScene().getWindow()).close();
        }catch (VideojuegoYaExisteException e){
            Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
        }
    }
}
