package main.command.list;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;

public class Head extends Command {
    public Head() {
        super("head", "get first element of collection");
    }

    @Override
    public Response execute(Request request){
        if (CollectionManager.getInstance().getCollection().isEmpty())
            return new Response("collection is empty");

        return new Response(CollectionManager.getInstance().getCollection().getFirst().toString() + '\n');
    }
}
