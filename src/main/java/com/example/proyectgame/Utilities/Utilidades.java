package com.example.proyectgame.Utilities;

import com.example.proyectgame.Exceptions.DatoNoValido;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Utilidades {



    public static LocalDate validarFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new DatoNoValido("La fecha introducida no es válida.");
        }
        return fecha;
    }

    public static boolean validarCorreo(String correo){
        String regex = "[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        return correo.matches(regex);
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
