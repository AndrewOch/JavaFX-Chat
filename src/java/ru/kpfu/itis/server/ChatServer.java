package ru.kpfu.itis.server;

import ru.kpfu.itis.client.Client;
import ru.kpfu.itis.model.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    private ServerSocket serverSocket;

    private List<Client> clients = new CopyOnWriteArrayList<>();

    private List<Room> rooms = new CopyOnWriteArrayList<>();

    private static ChatServer chatServer;

    public static ChatServer getInstance() {
        if (chatServer == null) {
            chatServer = new ChatServer();
        }
        return chatServer;
    }

    private ChatServer() {
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(8888);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rooms.add(new Room("11-003"));
                    rooms.add(new Room("Флуд"));
                    rooms.add(new Room("Друзья"));
                    while (true) {
                        try {
                            Socket socket = serverSocket.accept();
                            Client client = new Client(socket);
                            clients.add(client);
                            client.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            //console log
        }
    }

    public List<Client> getClients() {
        return clients;
    }

    public Client getClient(Socket socket) {
        System.out.println(clients);
        for (Client client : clients) {
            if (socket.equals(client.getClient())) {
                return client;
            }
        }
        return null;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}