package ru.kpfu.itis.server;

public class ServerApp {

    public static void main(String[] args) {
        ChatServer chatServer = ChatServer.getInstance();
        chatServer.start();
    }
}