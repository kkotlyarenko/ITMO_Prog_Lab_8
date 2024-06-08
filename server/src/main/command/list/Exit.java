package main.command.list;

import main.command.Command;
import main.console.Request;
import main.console.Response;

public class Exit extends Command{
    public Exit() {
        super("exit", "Close the program (without saving to file)");
    }

    @Override
    public Response execute(Request request) {
        System.exit(1);
        return new Response("Goodbye!");
    }
}
