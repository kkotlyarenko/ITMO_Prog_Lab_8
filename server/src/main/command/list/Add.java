package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;


public class Add extends Command {
    public Add() {
        super("add", "add a new element to the collection");
    }

    @Override
    public Response execute(Request request) {
        if (request.getCollection().isEmpty())
            return new Response("error! no elements in request!\n");
        if (request.getCollection().size()>1)
            return new Response("error! more than one element in request!\n");

        Route route = request.getCollection().iterator().next();
        route.setId(CollectionManager.getInstance().getFreeId());

        CollectionManager.getInstance().getCollection().add(route);
        return new Response(String.format("element with id %d added successfully!\n", route.getId()));
    }
}
