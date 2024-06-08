package main.command.list;

import main.collection.CollectionManager;
import main.collection.UserCollection;
import main.command.Command;
import main.console.Request;
import main.console.Response;

public class Info extends Command {
    public Info() {
        super("info", "Get information about the collection");
    }

    @Override
    public Response execute(Request request) {
        UserCollection collection = CollectionManager.getInstance().getCollection();
        return new Response(String.format("Collection type: %s\nInitialization date: %s\nSize: %d\n",
                collection.getClass().getName(), collection.getInitializedDate(), collection.size()));
    }
}
