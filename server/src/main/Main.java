package main;

import main.collection.CollectionManager;
import main.command.Command;
import main.command.list.*;
import main.command.handler.CommandHandler;
import main.console.ConsoleWorker;
import main.console.BaseConsoleWorker;
import main.format.FormatWorker;
import main.format.PostgresFormatWorker;
import main.models.Route;
import main.socket.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<Command> commands = List.of(
                new Info(),
                new Show(),
                new Add(),
                new UpdateById(),
                new RemoveById(),
                new Clear(),
//                new Save(),
                new ExecuteScript(),
//                new Exit(),
                new Head(),
                new AddIfMax(),
                new AddIfMin(),
                new RemoveAnyByDistance(),
                new CountLessThanDistance(),
                new PrintFieldAscendingDistance()
        );

        CommandHandler handler = new CommandHandler(commands);

        Server server = new Server(handler);

        Thread serverThread = new Thread(server);
        serverThread.start();

        new Thread(new Runnable() {
            private final BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in));

            @Override
            public void run() {
                String line;
                while (true) {
                    try {
                        if ((line=inputStream.readLine()) == null) break;

                        switch (line) {
                            case "save":
                                CollectionManager.getInstance().save();
                                break;
                            case "exit":
                                serverThread.interrupt();
                                CollectionManager.getInstance().save();
                                System.exit(0);
                                System.out.println("goodbye!");
                                System.exit(0);
                                break;
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }
}
