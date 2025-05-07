package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.example.proyectgame.Model.Usuario;

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
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Rellena todos los campos");
            errorLabel.setVisible(true);
            return;
        }

        Usuario usuario = UsuarioDAO.login(email, password);

        if (usuario != null) {
            System.out.println("Login correcto: " + usuario.getNombre());

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.close();

            // Luego podrías cargar otra vista aquí
        } else {
            errorLabel.setText("Credenciales incorrectas");
            errorLabel.setVisible(true);
        }
    }
}