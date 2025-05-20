module com.example.proyectgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.xml.bind;
    requires jbcrypt;


    opens com.example.proyectgame to javafx.fxml;
    opens com.example.proyectgame.Controller to javafx.fxml;
    exports com.example.proyectgame;
    opens com.example.proyectgame.DataBase to java.xml.bind;
    exports com.example.proyectgame.Controller;
}