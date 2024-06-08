package main.console;

import main.collection.UserCollection;
import main.command.Command;
import main.models.Route;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Response implements Serializable {
    private final String text;

    private final List<Route> routes;

    private final Deque<String> inboundRequests = new ArrayDeque<>();

    private List<Command> commands;

    public Response(String text, List<Route> routes) {
        this.text = text;
        this.routes = routes;
    }

    public Response(String text) {
        this(text, null);
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void addInboundRequest(String request) {
        inboundRequests.add(request);
    }

    public void addInboundRequests(Deque<String> requests) {
        inboundRequests.addAll(requests);
    }

    public Deque<String> getInboundRequests() {
        return inboundRequests;
    }

    public String getText() {
        return text;
    }

    public List<Route> getRoutes() {
        return routes;
    }
}
