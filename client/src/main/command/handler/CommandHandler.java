package main.command.handler;

import main.command.Command;
import main.console.Request;
import main.console.Response;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {
    private final List<Command> commands;

    public static volatile Deque<Request> requests = new ArrayDeque<>();

    public CommandHandler(List<Command> commands) {
        this.commands = commands;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Response handle(Request request) {
        requests.push(request);

        if (request == null)
            return new Response("ERROR! request cannot be empty.");

        if (request.getCommand() == null || request.getCommand().isBlank())
            return new Response("ERROR! command cannot be empty.");

        if (request.getCommand().equals("help"))
            return getHelp();

        List<Command> suitableCommands = commands.stream()
                .filter(command -> command.getName().equals(request.getCommand()))
                .toList();

        if (suitableCommands.isEmpty())
            return new Response("ERROR! unknown command: " + request.getCommand() + "\nenter 'help' for help.");
        if (suitableCommands.size() > 1)
            System.out.println("WARN! found several suitable commands: " + suitableCommands);

        return suitableCommands.get(0).execute(request);
    }

    public Response getHelp() {
        return new Response(commands.stream()
                .map(command -> String.format(" %-35s%-1s", command.getName(), command.getHelp()))
                .collect(Collectors.joining("\n")) + '\n');
    }
}
