package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;

public class Clear extends Command {
    public Clear() {
        super("clear", "clear the collection");
    }

    @Override
    public Response execute(Request request) {
        CollectionManager.getInstance().getCollection().clear();
        return new Response("all elements of the collection have been successfully deleted!\n");
    }
}
