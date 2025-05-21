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

    /**
     * Metodo de inicialización.
     * Carga en el ComboBox todos los valores del enum Genero.
     */
    @FXML
    private void initialize() {
        generoComboBox.getItems().setAll(Genero.values());

    }

    /**
     * Configura el formulario para edición de un videojuego existente.
     * Se llama antes de lanzar la vista.
     * Rellena los campos del formulario con los datos del videojuego recibido.
     * @param videojuego videojuego que se va a editar.
     */
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

    /**
     * Metodo que se ejecuta al pulsar el botón Guardar.
     * Decide si se llama a añadir un nuevo videojuego o actualizar uno existente según el estado de la variable esEdicion.
     * Muestra alertas en caso de que el nombre del videojuego ya exista.
     * @param actionEvent evento disparado por el botón.
     */
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

    /**
     * Añade un nuevo videojuego a la base de datos.
     * Valida que todos los campos del formulario estén rellenados y que una imagen de portada esté seleccionada.
     * Muestra alertas en caso de campos incompletos o datos no válidos.
     */
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

    /**
     * Actualiza un videojuego existente con los datos del formulario.
     * Muestra alerta si el nombre del videojuego ya existe.
     */
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

    /**
     * Permite al usuario seleccionar una imagen de su sistema.
     * Abre un diálogo para escoger archivos de imagen (.png, .jpg, .jpeg).
     * Copia el archivo seleccionado a la carpeta de recursos del proyecto para portadas.
     * @param actionEvent el evento se ejecuta al hacer clic en el botón de seleccionar imagen.
     */
    public void seleccionarImagen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de portada");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")); //formatos de imagen aceptados
        File archivo = fileChooser.showOpenDialog(null);

        if (archivo != null) {
            nombreImagenLabel.setText(archivo.getName());
            imagenSeleccionada = archivo;

            // Construimos el path absoluto del destino (desde la raíz del proyecto)
            File destino = new File("src/main/resources/com/example/proyectgame/Portadas/" + archivo.getName());

            try {
                java.nio.file.Files.copy(archivo.toPath(), destino.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
