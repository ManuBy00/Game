package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.UsuarioDAO;
import com.example.proyectgame.Utilities.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.example.proyectgame.Model.Usuario;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        // Permitir login al pulsar ENTER en el campo de contraseña
        passwordField.setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                EjecutarLogin();
            }
        });
    }

    @FXML
    public void EjecutarLogin() {
        //declaramos los datos que obtenemos del usuario
        String email = emailField.getText();
        String password = passwordField.getText();

        //cargamos la siguiente escena
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/VideojuegosView.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //lógica de iniciar sesión
        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Rellena todos los campos");
            errorLabel.setVisible(true);
            return;
        }

        Usuario usuario = UsuarioDAO.login(email, password);

        if (usuario != null) {
            System.out.println("Login correcto: " + usuario.getNombre());
            Sesion.getInstancia().logIn(usuario);

            VidejuegosViewController controller = loader.getController();
            controller.setVisibilidad(Sesion.getInstancia().getUsuarioIniciado());

            Scene videojuegosView = new Scene(root);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(videojuegosView);
            stage.show();

        } else {
            errorLabel.setText("Credenciales incorrectas");
            errorLabel.setVisible(true);
        }
    }

    public void formularioCrearCuenta(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/registro_usuario.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}