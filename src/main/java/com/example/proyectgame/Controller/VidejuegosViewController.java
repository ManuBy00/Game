package com.example.proyectgame.Controller;

import com.example.proyectgame.Model.RolUsuario;
import com.example.proyectgame.Model.Usuario;
import com.example.proyectgame.Utilities.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class VidejuegosViewController {

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;


    public void setVisibilidad(Usuario usuarioLogeado) {
        if (usuarioLogeado.getRolUsuario() != RolUsuario.ADMINISTRADOR) {
            addButton.setVisible(false);
            updateButton.setVisible(false);
            deleteButton.setVisible(false);
        }
    }
}
