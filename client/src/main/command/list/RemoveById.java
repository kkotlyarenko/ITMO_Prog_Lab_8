package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;

import java.util.ListIterator;

public class RemoveById extends Command {
    public RemoveById() {
        super("remove_by_id", "{id} - remove an item from the collection by its id");
    }

    @Override
    public Response execute(Request request) {
        if (request.getText() == null || request.getText().isEmpty())
            return new Response("error! no data in request!\n");
        if (!request.getText().matches("\\d+"))
            return new Response("error! request must be like | insert ID\n");

        int id = Integer.parseInt(request.getText());

        boolean isRemoved = false;
        ListIterator<Route> iterator = CollectionManager.getInstance().getCollection().listIterator();
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getId() == id) {
                iterator.remove();
                isRemoved = true;
                break;
            }
        }

        if (!isRemoved)
            return new Response("error! element with specified ID not found.\n");


        return new Response(String.format("element with id %d successfully removed\n", id));
    }
}
