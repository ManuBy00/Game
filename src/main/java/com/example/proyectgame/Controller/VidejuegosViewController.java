package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.VideojuegoDAO;
import com.example.proyectgame.Model.Genero;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Model.Videojuego;
import com.example.proyectgame.Utilities.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
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
    private ComboBox<Genero> comboGenero;
    @FXML
    private TextField buscadorNombre;

    /**
     * Metodo de inicialización del controlador
     * - Carga todos los géneros en el ComboBox.
     * - Añade un listener al campo de texto del buscador para aplicar el filtro en tiempo real.
     * - Carga todos los videojuegos desde la base de datos y los muestra en pantalla.
     *
     */
    @FXML
    public void initialize() {
        comboGenero.getItems().addAll(Genero.values());

        //listener
        buscadorNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltroBusqueda();
        });

        cargarVideojuegos();
    }

    /**
     * Aplica un filtro por género usando el ComboBox.
     * Si no hay género seleccionado, se muestran todos los videojuegos.
     * Si hay uno seleccionado, se consultan solo los videojuegos de ese género.
     */
    @FXML
    private void aplicarFiltroGenero(ActionEvent event) {
        Genero generoSeleccionado = comboGenero.getValue();

        VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
        List<Videojuego> videojuegos;

        if (generoSeleccionado == null) {
            videojuegos = videojuegoDAO.findAll();
        } else {
            videojuegos = videojuegoDAO.findByGenero(generoSeleccionado);
        }

        mostrarVideojuegos(videojuegos);
    }

    /**
     * Aplica un filtro combinando el texto del campo buscador y el género seleccionado.
     * Se filtran los videojuegos cuyo título contiene el texto buscado
     * y, si hay un género seleccionado, que también coincidan con él.
     */
    private void aplicarFiltroBusqueda() {
        String texto = buscadorNombre.getText().toLowerCase();
        Genero generoSeleccionado = comboGenero.getValue();

        VideojuegoDAO dao = new VideojuegoDAO();
        List<Videojuego> videojuegos = dao.findAll();

        List<Videojuego> filtrados = videojuegos.stream().filter(v -> v.getTitulo().toLowerCase().contains(texto))
                .filter(v -> generoSeleccionado == null || v.getGenero().equals(generoSeleccionado)).toList();

        mostrarVideojuegos(filtrados);
    }

    /**
     * Muestra en el GridPane los videojuegos recibidos.
     * Se colocan en una cuadrícula de 3 columnas por fila.
     * @param videojuegos Lista de videojuegos a mostrar
     */
    private void mostrarVideojuegos(List<Videojuego> videojuegos) {
        gridPaneVideojuegos.getChildren().clear(); // Limpiar anteriores

        int row = 0;
        int column = 0;

        for (Videojuego videojuego : videojuegos) {
            VBox vbox = crearCeldaJuego(videojuego);
            gridPaneVideojuegos.add(vbox, column, row);

            column++;
            if (column > 2) {
                column = 0;
                row++;
            }
        }
    }

    /**
     * Establece la visibilidad de los botones según el rol del usuario.
     * Si el usuario no es administrador, se ocultan los botones de añadir, editar y borrar.
     * @param usuarioLogeado El usuario que ha iniciado sesión
     */
    public void setVisibilidad(Usuario usuarioLogeado) {
        if (usuarioLogeado.getRolUsuario() != RolUsuario.ADMINISTRADOR) {
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }

    /**
     * Carga todos los videojuegos de la base de datos y los muestra en el GridPane.
     * Se usa una cuadrícula de 3 columnas por fila.
     */
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

    /**
     * Crea y devuelve un VBox que representa visualmente un videojuego en el GridPane.
     * Incluye la portada, el título y un botón para ver detalles.
     * También permite seleccionar la celda visualmente.
     * @param juego El videojuego que se va a mostrar
     * @return Un VBox que contiene la información del videojuego
     */
    private VBox crearCeldaJuego(Videojuego juego) {
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);

        Image imagen = cargarImagenPortada(juego);

        ImageView imageView = new ImageView(imagen);

        // Nombre del videojuego
        Text nombre = new Text(juego.getTitulo());
        nombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Botón para ver detalles (puedes agregar su lógica más adelante)
        Button button = new Button("Ver detalles");
        button.setOnAction(event -> verDetalles(juego));

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

    /**
     * Abre el formulario para insertar un nuevo videojuego.
     * Al cerrarlo, recarga la lista de videojuegos en pantalla.
     */
    public void lanzarFormularioInsert(ActionEvent actionEvent) {
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

    /**
     * Elimina el videojuego actualmente seleccionado.
     * Muestra una confirmación antes de borrar y recarga la vista si se confirma.
     */
    public void deleteVideojuego(ActionEvent actionEvent) {
        if (videojuegoSeleccionado != null) {
            VideojuegoDAO dao = new VideojuegoDAO();
            //pide confirmación
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar borrado");
            confirm.setHeaderText("¿Seguro que quieres eliminar este videojuego?");
            confirm.setContentText(videojuegoSeleccionado.getTitulo());
            confirm.showAndWait();
            //borra y deja los valores a null
            dao.delete(videojuegoSeleccionado.getId());
            videojuegoSeleccionado = null;
            celdaSeleccionada = null;
            gridPaneVideojuegos.getChildren().clear(); //limpiar grid
            cargarVideojuegos();
        } else {
            Utilidades.mostrarAlerta("Selección", "No hay ningún videojuego seleccionado");
        }
    }

    /**
     * Lanza el formulario de edición con los datos del videojuego seleccionado.
     * Tras cerrar el formulario, recarga la lista de videojuegos.
     */
    public void lanzarFormularioUpdate(ActionEvent actionEvent) {
        if (videojuegoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/formVideojuego.fxml"));
                Parent root = loader.load();

                FormVideojuegoController controller = loader.getController();
                controller.setVideojuego(videojuegoSeleccionado); // esta línea activa el modo edición del formulario

                Stage stage = new Stage();
                stage.setTitle("Editar videojuego");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                cargarVideojuegos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra la vista de detalles del videojuego seleccionado.
     * Carga el archivo FXML correspondiente y le pasa el videojuego al controlador.
     */
    @FXML
    private void verDetalles(Videojuego videojuego) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/detallesView.fxml"));
            Parent root = loader.load();

            DetalleVideojuegoController controller = loader.getController();
            controller.setVideojuego(videojuego);

            Stage stage = (Stage) addButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia el campo de búsqueda y recarga todos los videojuegos sin filtros.
     */
    @FXML
    public void limpiarFiltros(ActionEvent actionEvent) {
        buscadorNombre.clear();
        cargarVideojuegos();
    }

    /**
     * Abre la vista que muestra contenidos como noticias o guías.
     */
    @FXML
    public void lanzarVistaContenido(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/ContenidosView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la vista del perfil de usuario.
     */
    public void lanzarVistaUsuario(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/MiPerfilView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la imagen de portada de un videojuego.
     * Primero intenta cargarla como recurso de la aplicación.
     * Si no está empaquetada (por ejemplo, si se añadió en caliente), intenta desde el sistema de archivos.
     * Si todo falla, carga una imagen por defecto.
     * @param juego El videojuego cuya imagen se quiere cargar
     * @return La imagen lista para mostrar
     */
    private Image cargarImagenPortada(Videojuego juego) {
        String rutaRelativa = "/com/example/proyectgame/Portadas/" + juego.getPortada();

        // Primero intenta cargar la imagen del resource/portadas
        InputStream recursoStream = getClass().getResourceAsStream(rutaRelativa);
        if (recursoStream != null) {
            return new Image(recursoStream, 120, 160, true, true);
        }

        //Si la imagen se ha subido en tiempo de ejecución, no la va a encontrar, por lo que le damos la ruta completa
        File archivo = new File("src/main/resources/com/example/proyectgame/Portadas/" + juego.getPortada());
        if (archivo.exists()) {
            try {
                return new Image(new FileInputStream(archivo), 120, 160, true, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Si falla todo, carga la imagen por defecto
        InputStream defaultStream = getClass().getResourceAsStream("/com/example/proyectgame/Portadas/default.png");
        return new Image(defaultStream, 120, 160, true, true);
    }
}