package main.command.list;

import main.Main;
import main.collection.CollectionManager;
import main.command.Command;
import main.console.Request;
import main.console.Response;

public class Save extends Command {
    public Save() {
        super("save", "Save collection to file");
    }

    @Override
    public Response execute(Request request) {
        CollectionManager.getInstance().save();
        return new Response(null);
    }

}
