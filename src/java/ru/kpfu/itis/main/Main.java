package ru.kpfu.itis.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Чат");
        //FXMLLoader loader = new FXMLLoader().load(getClass().getResourceAsStream("\\fxml\\new.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));


        //Parent root = loader.load();
        stage.setScene(new Scene(root));

        stage.setWidth(1280);
        stage.setHeight(720);
        stage.setFullScreen(false);
        stage.setResizable(false);

        InputStream iconStream = getClass().getResourceAsStream("/images/logo.jpeg");
        if (iconStream != null) {
            Image image = new Image(iconStream);
            stage.getIcons().add(image);
        }
        stage.show();
    }
}
