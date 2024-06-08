package main.console;

import main.collection.UserCollection;

import java.io.Serializable;

public class Request implements Serializable {
    private String command;

    private UserCollection collection = new UserCollection();

    private String text;

    String login;

    String password;

    public Request(String command, UserCollection collection, String text, String login, String password) {
        this.command = command;
        this.collection = collection;
        this.text = text;
        this.login = login;
        this.password = password;
    }

    public Request() {}

    public String getCommand() {
        return command;
    }

    public UserCollection getCollection() {
        return collection;
    }

    public String getText() {
        return text;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCollection(UserCollection collection) {
        this.collection = collection;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
