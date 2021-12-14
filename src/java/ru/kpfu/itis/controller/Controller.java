package ru.kpfu.itis.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.protocol.Message;
import ru.kpfu.itis.protocol.MessageType;
import ru.kpfu.itis.sockets.ClientSocket;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ClientSocket clientSocket;

    @FXML
    private TextField username;
    @FXML
    private Button loginButton;

    @FXML
    private Button room1;
    @FXML
    private Button room2;
    @FXML
    private Button room3;

    @FXML
    private VBox messages;

    @FXML
    public TextField messageText;

    @FXML
    public Button sendMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginButton.setOnMouseClicked(event -> {
            String nickname = username.getText();
            loginButton.setDisable(true);
            username.setEditable(false);
            clientSocket = new ClientSocket();
            clientSocket.connect(this, nickname);
            room1.setDisable(false);
            room2.setDisable(false);
            room3.setDisable(false);
        });

        sendMessage.setOnAction(actionEvent -> {
            Message message = new Message();
            message.setType(MessageType.CHAT);
            message.setBody(messageText.getText());
            clientSocket.sendMessage(message);
            messageText.clear();
        });

        room1.setOnAction(actionEvent -> enterRoom(0));
        room2.setOnAction(actionEvent -> enterRoom(1));
        room3.setOnAction(actionEvent -> enterRoom(2));
    }

    public void enterRoom(Integer roomId) {
        Message message = new Message();
        message.setType(MessageType.ENTER_ROOM);
        message.setBody(roomId.toString());
        clientSocket.enterRoom(message);
    }

    public VBox getMessages() {
        return messages;
    }

    public TextField getMessageText() {
        return messageText;
    }

    public Button getSendMessage() {
        return sendMessage;
    }
}