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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class FormVideojuegoController {

    @FXML
    public Button imagenButton;
    @FXML
    private TextField tituloField;
    @FXML
    private TextArea descripcionArea;
    @FXML
    private ComboBox<Genero> generoComboBox;
    @FXML
    private DatePicker fechaLanzamiento;
    @FXML
    private TextField desarrolladorField;
    @FXML
    private Button guardarButton;
    @FXML
    private Videojuego videojuegoSeleccionado;
    @FXML
    private Label nombreImagenLabel;
    @FXML
    private File imagenSeleccionada;

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
        if (tituloField==null || descripcionArea == null || generoComboBox == null || desarrolladorField == null || imagenSeleccionada==null){
            Utilidades.mostrarAlerta("Campos incompletos", "Debes rellenar todos los campos");
            return;
        }
        try {
            String titulo = tituloField.getText();
            String descripcion = descripcionArea.getText();
            Genero generoEnum = generoComboBox.getValue();
            LocalDate fecha = Utilidades.validarFecha(fechaLanzamiento.getValue());
            String desarrollador = desarrolladorField.getText();
            String portada = imagenSeleccionada.getName();

            Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, generoEnum, fecha, portada);
            VideojuegoDAO dao = new VideojuegoDAO();

            dao.insert(nuevo);
            ((Stage) guardarButton.getScene().getWindow()).close(); // Cierra la ventana
        }catch (DatoNoValido e){
            Utilidades.mostrarAlerta("Error", e.getMessage());
        }catch (VideojuegoYaExisteException e) {
            Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
        }
    }

    public void updateVideojuego(){
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        Genero genero = generoComboBox.getValue();
        LocalDate fecha = fechaLanzamiento.getValue();
        String desarrollador = desarrolladorField.getText();
        String portada = videojuegoSeleccionado.getPortada();

        //si hay una imagen nueva, se cambia a esta
        if (imagenSeleccionada!=null){
            portada = imagenSeleccionada.getName();
        }

        Videojuego nuevo = new Videojuego(titulo, descripcion, desarrollador, genero, fecha, portada);
        VideojuegoDAO dao = new VideojuegoDAO();

        try{
            dao.update(nuevo, videojuegoSeleccionado);
            ((Stage) guardarButton.getScene().getWindow()).close();
        }catch (VideojuegoYaExisteException e){
            Utilidades.mostrarAlerta("Nombre duplicado", e.getMessage());
        }
    }

    public void seleccionarImagen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de portada");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            nombreImagenLabel.setText(archivo.getName());
            imagenSeleccionada = archivo;

            // Construimos el path absoluto del destino (desde la raíz del proyecto)
            File destino = new File("src/main/resources/com/example/proyectgame/Portadas/" + archivo.getName());

            try {
                java.nio.file.Files.copy(
                        archivo.toPath(),
                        destino.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
