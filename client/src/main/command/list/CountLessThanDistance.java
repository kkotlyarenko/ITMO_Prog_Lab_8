package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;

import java.util.ListIterator;

public class CountLessThanDistance extends Command {
    public CountLessThanDistance() {
        super("count_less_than_distance", "{distance} - display the number of elements, the value of the distance field of which is less than the specified one.");
    }

    @Override
    public Response execute(Request request) {
        if (request.getText() == null || request.getText().isEmpty())
            return new Response("error! no data in request!\n");
        if (!request.getText().matches("\\d+"))
            return new Response("error! request must be like | insert Distance\n");

        long distance = Long.parseLong(request.getText());

        return new Response("Number of elements with distance less than "
                + distance
                + ": " + CollectionManager.getInstance().getCollection().stream().filter(route -> route.getDistance() < distance).count() + '\n');
    }
}
