package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;

import java.util.List;
import java.util.stream.Collectors;

public class Show extends Command{
    public Show() {
        super("show", "Show all elements from collection");
    }

    @Override
    public Response execute(Request request) {
         String text = "Collection:\n" + CollectionManager.getInstance().getCollection()
                .stream().map(route -> route.toString() + '\n')
                .collect(Collectors.joining());
        return new Response(text, CollectionManager.getInstance().getCollection());
    }
}
