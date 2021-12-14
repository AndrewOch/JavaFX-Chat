package ru.kpfu.itis.model;

public class Message {

    private Client author;
    private Integer type;
    private String content;

    private final int TEXT = 1;
    private final int EXIT_CHAT = 2;
    private final int FILE_SEND = 3;
    private final int FILE_RECEIVE = 4;

    public Message(Client author, Integer type, String content) {
        this.author = author;
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString() {
        return switch (type) {
            case TEXT -> author.getUsername() + ": " + content + "\n";
            case EXIT_CHAT -> author.getUsername() + " exited the chat\n";
            case FILE_SEND -> author.getUsername() + " sending a file\n";
            case FILE_RECEIVE -> author.getUsername() + " received file\n";
            default -> author.getUsername() + ": " + content + "\n";
        };
    }


    public Client getAuthor() {
        return author;
    }

    public void setAuthor(Client author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
