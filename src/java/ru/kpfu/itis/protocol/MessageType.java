package ru.kpfu.itis.protocol;

public enum MessageType {
    CONNECT("connect", "Подключение к серверу"),
    ENTER_ROOM("enter room", "Вход в комнату"),
    EXIT_ROOM("exit room", "Выход из комнаты"),
    CHAT("chat", "Сообщение другим пользователям"),
    FILE("file", "Отправка файла");


    private final String title;
    private final String description;

    MessageType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}