package main.command;

import main.console.Request;
import main.console.Response;

import java.io.Serializable;

public abstract class Command implements Serializable {
    private final String name;
    private final String help;

    private final int elementsRequired;

    public Command(String name, String help) {
        this.name = name;
        this.help = help;
        this.elementsRequired = 0;
    }

    public Command(String name, String help, int elementsRequired) {
        this.name = name;
        this.help = help;
        this.elementsRequired = elementsRequired;
    }

    public Command(String name) {
        this(name, String.format("No help for '%s' command", name));
    }

    public String getName() {
        return name;
    }

    public String getHelp() {
        return help;
    }

    public int getElementsRequired() {
        return elementsRequired;
    }

    public abstract Response execute(Request request);
}
