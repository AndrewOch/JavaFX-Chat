package ru.kpfu.itis.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextField name;
    public TextField group;
    public TextField graduationYear;
    public TextField university;

    public ScrollPane messagesArea;
    public VBox messages;

    @FXML
    private Button addUser;


    private void updateMessagesOutput() {
        messages.getChildren().clear();

//        for (User user : users) {
//            Label messageLabel = new Label();
//            messageLabel.setText(user.toString());
//            messages.getChildren().add(messageLabel);
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //addUser.setOnAction(actionEvent -> add_user_clicked());
        updateMessagesOutput();
    }

}