package ru.kpfu.itis.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.kpfu.itis.protocol.Message;
import ru.kpfu.itis.protocol.MessageType;
import ru.kpfu.itis.server.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client extends Thread {

    private String nickname;

    private Integer roomId = -1;

    private Socket client;

    // поток символов, которые мы отправляем клиенту
    private PrintWriter toClient;

    // поток символов, которые мы читаем от клиента
    private BufferedReader fromClient;

    private ChatServer chatServer;

    public Client(Socket client) {
        chatServer = ChatServer.getInstance();
        this.client = client;
        try {
            // обернули потоки байтов в потоки символов
            this.toClient = new PrintWriter(client.getOutputStream(), true);
            this.fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // мы в любое время можем получить сообщение от клиента
    // поэтому чтение сообщения от клиента должно происходить в побочном потоке
    @Override
    public void run() {
        while (true) {
            // прочитали сообщение от клиента
            String messageFromClient;

            try {
                messageFromClient = fromClient.readLine();
                if (messageFromClient != null) {
                    System.out.println(nickname + ": " + messageFromClient);

                    Message message = new ObjectMapper().readValue(messageFromClient, Message.class);

                    List<Client> clients = chatServer.getClients();

                    switch (message.getType()) {
                        case CONNECT: {
                            this.nickname = message.getHeaders().get("nickname");
                            System.out.println(nickname);
                            break;
                        }
                        case ENTER_ROOM: {
                            setRoomId(Integer.parseInt(message.getBody()));
                            Message alarm = new Message();
                            alarm.setType(MessageType.CHAT);
                            alarm.setBody(nickname + " вошёл в комнату");
                            for (Message m : chatServer.getRooms().get(getRoomId()).getMessages()) {
                                this.sendMessage(m);
                            }
                            if (!this.getRoomId().equals(-1)) {
                                chatServer.getRooms().get(this.getRoomId()).addMessage(alarm);
                            }
                            for (Client client : clients) {
                                if (this.getRoomId() != -1 && client.getRoomId().equals(this.getRoomId())) {
                                    System.out.println(client.nickname);
                                    client.sendMessage(alarm);
                                }
                            }
                            sendMessage(message);
                            break;
                        }
                        case EXIT_ROOM: {
                            Message alarm = new Message();
                            alarm.setType(MessageType.CHAT);
                            alarm.setBody(nickname + " вышел из комнаты");
                            if (!this.getRoomId().equals(-1)) {
                                chatServer.getRooms().get(this.getRoomId()).addMessage(alarm);
                            }
                            for (Client client : clients) {
                                if (this.getRoomId() != -1 && client.getRoomId().equals(this.getRoomId())) {
                                    System.out.println(client.nickname);
                                    client.sendMessage(alarm);
                                }
                            }
                            sendMessage(message);
                            break;
                        }
                        case CHAT: {
                            message.setBody(nickname + ": " + message.getBody());
                            if (!this.getRoomId().equals(-1)) {
                                chatServer.getRooms().get(this.getRoomId()).addMessage(message);
                            }
                            for (Client client : clients) {
                                if (client.getRoomId().equals(this.getRoomId())) {
                                    System.out.println(client.nickname);
                                    client.sendMessage(message);
                                }
                            }
                            break;
                        }
                        case FILE: {
                            Message alarm = new Message();
                            alarm.setType(MessageType.CHAT);
                            alarm.setBody(nickname + " отправил файл " + message.getHeaders().get("fileName"));
                            if (!this.getRoomId().equals(-1)) {
                                chatServer.getRooms().get(this.getRoomId()).addMessage(alarm);
                            }
                            for (Client client : clients) {
                                if (this.getRoomId() != -1 && client.getRoomId().equals(this.getRoomId())) {
                                    System.out.println(client.nickname);
                                    client.sendMessage(alarm);
                                    if (!this.equals(client)) {
                                        client.sendMessage(message);
                                    }
                                }
                            }
                            break;
                        }
                    }

                }
            } catch (IOException e) {
                //
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            String jsonMessage = new ObjectMapper().writeValueAsString(message);
            System.out.println("SEND message TO: " + nickname + " " + jsonMessage);
            toClient.println(jsonMessage);
        } catch (JsonProcessingException e) {
            //console log
        }
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Socket getClient() {
        return client;
    }
}