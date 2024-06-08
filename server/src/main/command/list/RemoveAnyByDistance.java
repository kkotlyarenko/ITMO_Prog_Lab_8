package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;

import java.util.ListIterator;

public class RemoveAnyByDistance extends Command {
    public RemoveAnyByDistance() {
        super("remove_any_by_distance", "{distance} - remove any element from the collection by its distance.");
    }

    @Override
    public Response execute(Request request) {
        if (request.getText() == null || request.getText().isEmpty())
            return new Response("error! no data in request!\n");
        if (!request.getText().matches("\\d+"))
            return new Response("error! request must be like | insert Distance\n");

        long distance = Long.parseLong(request.getText());
        int id = 0;

        boolean isRemoved = false;
        ListIterator<Route> iterator = CollectionManager.getInstance().getCollection().listIterator();
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getDistance() == distance) {
                id = route.getId();
                iterator.remove();
                isRemoved = true;
                break;
            }
        }

        if (!isRemoved)
            return new Response("error! element with specified Distance not found.\n");


        return new Response(String.format("element with id %d successfully removed\n", id));
    }
}
