package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;
import main.models.Route;

import java.util.ListIterator;

public class UpdateById extends Command {
    public UpdateById() {
        super("update", "{id} {element} - Update element by id");
    }

    @Override
    public Response execute(Request request) {
        if (request.getText() == null || request.getText().isEmpty())
            return new Response("error! no data in request!\n");
        if (!request.getText().matches("\\d+"))
            return new Response("error! request must be like | insert ID {element}\n");
        if (request.getCollection().isEmpty())
            return new Response("error! at least one element must be entered\n");

        int id = Integer.parseInt(request.getText());

        if (request.getCollection().isEmpty())
            return new Response("error! collection is empty\n");

        Route inputElement = request.getCollection().getFirst();

        inputElement.setId(id);

        boolean isUpdated = false;
        ListIterator<Route> iterator = CollectionManager.getInstance().getCollection().listIterator();
        while (iterator.hasNext()) {
            Route route = iterator.next();
            if (route.getId() == id) {
                iterator.set(inputElement);
                isUpdated = true;
                break;
            }
        }

        if (!isUpdated)
            return new Response("error! element with specified ID not found.\n");


        return new Response(String.format("element with id %d successfully updated\n", id));
    }
}
