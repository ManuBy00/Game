package com.example.proyectgame.Controller;

import com.example.proyectgame.DAO.UsuarioDAO;
import com.example.proyectgame.Exceptions.UsuarioExiste;
import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.example.proyectgame.Utilities.Utilidades.mostrarAlerta;
import static com.example.proyectgame.Utilities.Utilidades.validarCorreo;

public class RegistroUsuarioController {
    @FXML
    private TextField nombreField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private ComboBox<RolUsuario> rolComboBox;

    @FXML
    private Button registrarButton;


    /**
     * Metodo de inicialización.
     * Carga todos los roles (Administrador y Jugador) en el ComboBox.
     */
    @FXML
    public void initialize() {
        // Llenar comboBox con roles
        rolComboBox.setItems(FXCollections.observableArrayList(RolUsuario.ADMINISTRADOR, RolUsuario.JUGADOR));
    }

    /**
     * Metodo que se ejecuta al pulsar el botón "Registrar".
     * Recoge los datos del formulario (nombre, email, contraseña, rol).
     * Valida que todos los campos estén rellenos y que el correo tenga un formato válido.
     * Si todo es correcto, crea un nuevo objeto Usuario y lo guarda en la base de datos mediante el DAO.
     * Si el usuario ya existe, muestra un mensaje de error.
     * Si el registro es exitoso, muestra un mensaje de confirmación y cierra la ventana de registro.
     *
     * @param actionEvent Evento que dispara la acción (clic del botón).
     */
    public void registrarUsuario(ActionEvent actionEvent) {
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String contrasena = contrasenaField.getText();
        RolUsuario rol = rolComboBox.getValue();

        if (nombre.isEmpty() || email.isEmpty() || contrasena.isEmpty() || rol == null) {
            mostrarAlerta("Error", "Por favor, rellena todos los campos.");
            return;
        }

        if (!validarCorreo(email)){
            mostrarAlerta("Error", "Por favor, introduce un correo válido.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, email, contrasena, rol);

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        try{
            if (usuarioDAO.insert(nuevoUsuario)){
                mostrarAlerta("Éxito", "Usuario registrado correctamente.");
            }
        }catch (UsuarioExiste e){
            mostrarAlerta("Error", e.getMessage());
        }

        // Cerrar ventana
        Stage stage = (Stage) registrarButton.getScene().getWindow();
        stage.close();
    }
}
