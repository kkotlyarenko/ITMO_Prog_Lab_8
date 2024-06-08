package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;

public class AddIfMin extends Command {
    public AddIfMin() {
        super("add_if_min", "{element} - add a new element to the collection if its value is less than the minimum value of the collection element.");
    }

    @Override
    public Response execute(Request request) {
        if (request.getCollection().isEmpty())
            return new Response("error! no elements in request!\n");
        if (request.getCollection().size()>1)
            return new Response("error! more than one element in request!\n");

        if (request.getCollection().getFirst().compareTo(CollectionManager.getInstance().getMin()) < 0) {
            CollectionManager.getInstance().getCollection().add(request.getCollection().getFirst());
            return new Response("element added successfully!\n");
        } else {
            return new Response("element not added!\n");
        }
    }
}
