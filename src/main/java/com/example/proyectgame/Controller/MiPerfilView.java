package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.ResenaDAO;
import com.example.proyectgame.DAO.UsuarioDAO;
import com.example.proyectgame.Model.Resena;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Utilities.Sesion;
import com.example.proyectgame.Utilities.Utilidades;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MiPerfilView {
    @FXML
     private TextField passwordField;
    @FXML
    private TextField usuarioField;
    @FXML
     private ListView<Resena> ResenasListView;
    @FXML
     private Label usuarioActualLabel;

    private Usuario usuario = Sesion.getInstancia().getUsuarioIniciado();


    @FXML
    public void initialize() {
        cargarListaResenas();
        setUsuarioActualLabel(usuario.getNombre());
    }

    public void cargarListaResenas(){
        ResenaDAO resenaDAO = new ResenaDAO();
        List<Resena> listaResenasUsuario = resenaDAO.findByUsuario(usuario.getId());
        ResenasListView.getItems().setAll(listaResenasUsuario);

        ResenasListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Resena resena, boolean empty) {
                super.updateItem(resena, empty);
                if (empty || resena == null) {
                    setText(null);
                } else {
                    setText("[" + resena.getVideojuego().getTitulo() + "] " +
                            resena.getComentario() + " - " + resena.getPuntuacion() + "/5");
                }
            }
        });
    }

    public void setUsuarioActualLabel(String usuarioActual){
        usuarioActualLabel.setText("Usuario: " + usuarioActual);
    }

    public void guardarCambiosUsuario(ActionEvent actionEvent) {
        String password = passwordField.getText();
        String nombre = usuarioField.getText();

        Usuario usuarioNuevo = usuario;

        if (!password.isEmpty()) {
            usuarioNuevo.setContrasena(password);
        }

        if (!nombre.isEmpty()) {
            usuarioNuevo.setNombre(nombre);
        }
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.update(usuarioNuevo, usuario);
        setUsuarioActualLabel(usuarioNuevo.getNombre());
        Utilidades.mostrarAlerta("Usuario actualizado", "Usuario actualizado correctamente");
    }

    @FXML
    public void lanzarVistaContenido(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/ContenidosView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usuarioActualLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lanzarVistaVideojuegos(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/VideojuegosView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usuarioActualLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void borrarResena(ActionEvent actionEvent) {
        Resena resena = ResenasListView.getSelectionModel().getSelectedItem();
        if (resena == null) {
        }
        ResenaDAO resenaDAO = new ResenaDAO();

        if(resenaDAO.delete(resena.getId())){
            Utilidades.mostrarAlerta("Resena eliminada", "Resena eliminada correctamente");
        }else{
            Utilidades.mostrarAlerta("Error", "Hubo un error al eliminar la resena");
        }
        cargarListaResenas();
    }
}
