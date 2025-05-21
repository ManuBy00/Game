package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.GuiaDAO;
import com.example.proyectgame.DAO.NoticiaDAO;
import com.example.proyectgame.Model.*;
import com.example.proyectgame.Utilities.Sesion;
import com.example.proyectgame.Utilities.Utilidades;
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
    public Button deteleButton;
    @FXML
    private Button addButton;

    @FXML
    private Button updateButton;


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

    /**
     * Inicializa la vista del contenido.
     * Configura cómo se muestran los elementos de la listView.
     * Carga los contenidos (noticias y guías).
     * Ajusta la visibilidad de los botones según el usuario logeado.
     */
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
        setVisibilidad(Sesion.getInstancia().getUsuarioIniciado());
    }

    /**
     * Carga todas las noticias y guías desde la base de datos
     * y las añade a la lista de contenidos para mostrarlas en el listView de contenidos.
     */
    private void cargarContenidos() {
        List<Noticia> noticias = noticiaDAO.findAll();
        List<Guia> guias = guiaDAO.findAll();

        List<Contenido> contenidos = new ArrayList<>();
        contenidos.addAll(noticias);
        contenidos.addAll(guias);

        contenidosList.getItems().setAll(contenidos);
    }

    /**
     * Muestra los detalles del contenido seleccionado en la interfaz al hacer clic con el ratón.
     * @param event evento del ratón
     */
    @FXML
    private void mostrarContenidoSeleccionado(MouseEvent event) {
        contenidoSeleccionado = contenidosList.getSelectionModel().getSelectedItem();
        if (contenidoSeleccionado != null) {
            mostrarContenido(contenidoSeleccionado);
        }
    }

    /**
     * Muestra los datos del contenido en los labels de la interfaz.
     * Si es una Noticia muestra su subtítulo; si es una Guía, el título del videojuego.
     * @param contenido contenido a mostrar
     */
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

        autorLabel.setText("Autor: " + contenido.getAutor().getNombre());

        if (contenido.getFechaPublicacion() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaLabel.setText("Fecha: " + contenido.getFechaPublicacion().format(formatter));
        } else {
            fechaLabel.setText("Fecha: Desconocida");
        }
    }

    /**
     * Abre el formulario de inserción o edición de contenido.
     * Si se pasa un contenido, se abre en modo edición (esto lo hace le metodo setContenido()).
     * @param contenido contenido a editar o null para crear uno nuevo
     */
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
            stage.showAndWait(); //


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lanza el formulario para insertar un nuevo contenido.
     */
    @FXML
    private void lanzarInsertForm() {
        abrirFormulario(null);
        cargarContenidos();
    }

    /**
     * Lanza el formulario para editar el contenido seleccionado.
     * Si no hay contenido seleccionado, muestra una alerta.
     */
    @FXML
    private void lanzarUpdateForm() {
        if (contenidoSeleccionado == null) {
            Utilidades.mostrarAlerta("Atención", "Selecciona un contenido para editar.");
            return;
        }
        abrirFormulario(contenidoSeleccionado);
        cargarContenidos();
    }

    /**
     * Elimina el contenido seleccionado si el usuario confirma la acción.
     * Muestra una alerta si no hay ningún contenido seleccionado.
     */
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
            Utilidades.mostrarAlerta("Atención","Selecciona un contenido para borrar.");
        }
    }

    /**
     * Limpia los campos de la interfaz.
     */
    private void limpiarVista() {
        tituloLabel.setText("TITULO");
        subtituloLabel.setText("SUBTITULO");
        cuerpoLabel.setText("Cuerpo");
        autorLabel.setText("Autor");
        fechaLabel.setText("Fecha label");
    }

    /**
     * Navega a la vista del perfil de usuario.
     * @param actionEvent evento del botón
     */
    public void lanzarVistaUsuario(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/MiPerfilView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tituloLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navega a la vista de videojuegos.
     * @param actionEvent evento del botón
     */
    public void lanzarVistaVideojuegos(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/VideojuegosView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) autorLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recarga los contenidos mostrados en la vista.
     * @param actionEvent evento del botón
     */
    public void lanzarVistaContenido(ActionEvent actionEvent) {
        cargarContenidos();
    }

    /**
     * Ajusta la visibilidad de los botones según el rol del usuario logeado.
     * @param usuarioLogeado el usuario actualmente en sesión
     */
    public void setVisibilidad(Usuario usuarioLogeado) {
        if (usuarioLogeado.getRolUsuario() != RolUsuario.ADMINISTRADOR) {
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deteleButton.setVisible(false);
        }
    }
}