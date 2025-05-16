package com.example.proyectgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectgame/LoginView.fxml"));

        Parent root = loader.load();

        primaryStage.setTitle("Iniciar sesión");
        primaryStage.setScene(new Scene(root, 350, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}