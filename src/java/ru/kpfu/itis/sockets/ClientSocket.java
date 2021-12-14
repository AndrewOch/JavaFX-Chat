package ru.kpfu.itis.sockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import ru.kpfu.itis.client.Client;
import ru.kpfu.itis.controller.Controller;
import ru.kpfu.itis.model.Room;
import ru.kpfu.itis.protocol.Message;
import ru.kpfu.itis.protocol.MessageType;
import ru.kpfu.itis.server.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket extends Thread {

    private Socket clientSocket;

    private ChatServer chatServer;

    private final String HOST = "localhost";
    private final int PORT = 8888;

    private PrintWriter out;
    private BufferedReader fromServer;

    private Controller controller;

    public void connect(Controller controller, String nickname) {
        try {
            this.controller = controller;
            clientSocket = new Socket(HOST, PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            chatServer = ChatServer.getInstance();

            Message message = new Message();
            message.setType(MessageType.CONNECT);
            message.addHeader("nickname", nickname);
            sendMessage(message);
            this.start();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void sendMessage(Message message) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(message);
            System.out.println(jsonMessage);
            out.println(jsonMessage);
        } catch (JsonProcessingException e) {
            //console log
        }
    }

    public void enterRoom(Message message) {
        try {
            Message exitMessage = new Message();
            exitMessage.setType(MessageType.EXIT_ROOM);
            String jsonMessage = new ObjectMapper().writeValueAsString(exitMessage);
            System.out.println(jsonMessage);
            out.println(jsonMessage);

            jsonMessage = new ObjectMapper().writeValueAsString(message);
            System.out.println(jsonMessage);
            out.println(jsonMessage);
        } catch (JsonProcessingException e) {
            //console log
        }
    }

    @Override
    public void run() {
        while (true) {
            String messageFromServer;
            Message message = null;
            try {
                messageFromServer = fromServer.readLine();
                System.out.println(messageFromServer);
                message = new ObjectMapper().readValue(messageFromServer, Message.class);
                System.out.println(message);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            switch (message.getType()) {
                case CHAT: {
                    Label label = new Label();
                    label.setText(message.getBody());
                    label.setFont(Font.font("Arial"));
                    Platform.runLater(() -> controller.getMessages().getChildren().add(label));
                    break;
                }
                case EXIT_ROOM: {
                    Label label = new Label();
                    label.setText(message.getBody());
                    label.setFont(Font.font("Arial"));
                    Platform.runLater(() -> controller.getMessages().getChildren().clear());
                    break;
                }
                case ENTER_ROOM: {
                    Platform.runLater(() -> controller.getMessageText().setDisable(false));
                    Platform.runLater(() -> controller.getSendMessage().setDisable(false));
                    break;
                }
            }
        }
    }
}