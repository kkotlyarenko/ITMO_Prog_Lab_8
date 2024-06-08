package main.command.list;

import main.command.Command;
import main.console.Request;
import main.console.Response;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;

public class ExecuteScript extends Command {
    public ExecuteScript() {
        super("execute_script", "{filename} - read and execute script from specified file.");
    }

    @Override
    public Response execute(Request request){
        if (request.getText() == null || request.getText().isEmpty())
            return new Response("error! request is empty!");
        if (Files.notExists(Paths.get(request.getText())))
            return new Response("error! specified file does not exist");

        Deque<String> inboundRequests = new ArrayDeque<>();

        try(BufferedReader fileReader = new BufferedReader(new FileReader(request.getText()))) {
            while (fileReader.ready())
                inboundRequests.push(fileReader.readLine());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Response response = new Response(null);
        response.addInboundRequests(inboundRequests);

        return response;
    }
}
