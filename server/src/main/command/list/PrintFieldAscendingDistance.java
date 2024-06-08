package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;

import java.util.stream.Collectors;

public class PrintFieldAscendingDistance extends Command {
    public PrintFieldAscendingDistance() {
        super("print_field_ascending_distance", "print the value of the distance field of all elements in ascending order.");
    }

    @Override
    public Response execute(Request request) {
        return new Response("Collection:\n" + CollectionManager.getInstance().getCollection()
                .stream().sorted().map(route -> route.toString() + '\n')
                .collect(Collectors.joining()));
    }
}
