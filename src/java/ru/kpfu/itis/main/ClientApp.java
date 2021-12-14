package ru.kpfu.itis.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class ClientApp extends Application {

    private static final String FXML_FILE_NAME = "/fxml/main.fxml";
    private static final String ICON_FILE_NAME = "/images/logo.jpeg";

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Чат");
        Parent root = FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));

        stage.setScene(new Scene(root));

        stage.setWidth(720);
        stage.setHeight(1280);
        stage.setFullScreen(false);
        stage.setResizable(false);

        InputStream iconStream = getClass().getResourceAsStream(ICON_FILE_NAME);
        if (iconStream != null) {
            Image image = new Image(iconStream);
            stage.getIcons().add(image);
        }
        stage.show();
    }
}
