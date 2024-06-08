package main.socket;

import main.collection.CollectionManager;
import main.command.Command;
import main.console.ConsoleWorker;
import main.console.Request;
import main.console.Response;
import main.gui.LoginWindow;
import main.models.Coordinates;
import main.models.LocationFrom;
import main.models.LocationTo;
import main.models.Route;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Client implements Runnable, Serializable {
//    private static final ConsoleWorker consoleWorker;

    private static Socket server;
    public static ObjectOutputStream objectOutputStream;
    public static ObjectInputStream objectInputStream;

    private static int reconnectCount = 0;

    private static List<Command> serverCommands = Collections.emptyList();

    public static String username;
    public static String password;

    public static ResourceBundle bundle;
    public static JFrame frame;

    public Client(){}
    @Override
    public void run() {
        SwingUtilities.invokeLater(() -> {
            Locale locale = new Locale("ru", "RU"); // Локаль по умолчанию
            bundle = ResourceBundle.getBundle("messages", locale);
            LoginWindow.showLoginWindow(locale);
        });
    }

    private void handle(String input) throws Throwable {
        // check system commands
        switch (input) {
            case "reconnect": // reconnect to server
                shutdownGracefully();
//                connect();
                break;
            case "exit": // exit
                shutdownGracefully();
                System.exit(0);
                break;
            default: // send request and get response
                Request request = parseString(input);
                if (request == null) return;

                Response response = sendAndGet(request);

                if (response == null) {
//                    consoleWorker.print("Server returned null response");
                    return;
                }

                if (response.getText() != null && (response.getText().equals("Incorrect username/password") || response.getText().equals("Enter username/password!"))) {
//                    consoleWorker.print(response.getText());
                    System.exit(1);
                    return;
                }

//                if (response.getText() != null) consoleWorker.print(response.getText());
                if (response.getRoutes() != null) {
//                    response.getRoutes().forEach(route -> consoleWorker.print(route.toString()));
                }

                break;
        }
    }

    private Request parseString(String input) throws Throwable {
        Request request = new Request();

        request.setLogin(this.username);
        request.setPassword(this.password);

        while (input.contains("element")) {
            input = input.replaceFirst("element", "");
            Route inputRoute = inputRoute();

            request.getCollection().add(inputRoute);
        }

        String[] parts = input.trim().split(" ", 2);
        request.setCommand(parts[0]);

        int requiredElements = serverCommands.stream()
                .filter(command -> command.getName().equalsIgnoreCase(request.getCommand()))
                .map(Command::getElementsRequired).findAny().orElse(0);

        while (requiredElements-- > 0) {
            try {
                request.getCollection().add(inputRoute());
            } catch (Throwable t) {
//                consoleWorker.print("Exiting command: " + t.getMessage());
                return null;
            }
        }

        if (parts.length == 2) request.setText(parts[1].trim());

        return request;
    }

    public static Response sendAndGet(Request request) {
        sendRequest(request);
        try {
            return (Response) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
//            consoleWorker.error("Error receiving answer: " + e.getMessage());
            return null;
        }
    }

    public static void sendRequest(Request request) {
        try {
            objectOutputStream.writeObject(request);
        } catch (IOException e) {
//            consoleWorker.error("Error sending request: " + e.getMessage());
//            consoleWorker.error("Use 'reconnect' to reconnect to server");
        }
    }

    private static void shutdownGracefully() {
        try {
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
            if (server != null) server.close();

            objectInputStream = null;
            objectOutputStream = null;
            server = null;

//            consoleWorker.print("\nConnection closed\n");
        } catch (IOException e) {
//            consoleWorker.error("Error closing threads:" + e.getMessage());
        }
    }

    public static int connect(String username, String password) {
        try {
            server = new Socket("localhost", 8080);
            server.setSoTimeout(1000);
            objectOutputStream = new ObjectOutputStream(server.getOutputStream());
            objectInputStream = new ObjectInputStream(server.getInputStream());

            Response response = sendAndGet(new Request("CONNECT", null, null, username, password));

            if (response != null) {
                serverCommands = response.getCommands();
                String message = response.getText();

                if (message != null) {
                    if (message.equals("Enter username/password!") || message.equals("Incorrect username/password")) {
//                    consoleWorker.print(message);
                        return -2;
                    }

//                consoleWorker.print("connection established successfully: " + message);

                    if (message.equals("Incorrect username/password") || message.equals("Enter username/password!")) {
                        return -1;
                    }
                    if (message.equals("User created")) {
                        return 0;
                    }
                    return 1;
                }
            }

        } catch (IOException e) {
            System.err.println("Error connectiong to server. Reconnect in " + 10 + " seconds | Tries: " + reconnectCount + "/" + 5);
            shutdownGracefully();

            try {
                TimeUnit.SECONDS.sleep(10);

                if (reconnectCount++ > 5) {
                    return -1;
                }

//            connect();
            } catch (InterruptedException ex) {
//            consoleWorker.error("Error waiting connection");
                return -1;
            }
        }
        return -2;
    }

    private Route inputRoute() throws Throwable {
//        consoleWorker.print("New Route:");
//        consoleWorker.skip();
//        consoleWorker.print("Enter main information:");

        Route route = new Route();
        route.setCreatedBy(this.username);
        while(!input("name", route::setName, str->str));

//        consoleWorker.skip();
//        consoleWorker.print("Enter coordinates:");
        Coordinates coordinates = new Coordinates();
        while(!input("x", coordinates::setX, Long::parseLong));
        while(!input("y", coordinates::setY, Double::parseDouble));
        route.setCoordinates(coordinates);

//        consoleWorker.skip();
//        consoleWorker.print("Enter location from:");
        LocationFrom locationFrom = new LocationFrom();
        while(!input("x", locationFrom::setX, Long::parseLong));
        while(!input("y", locationFrom::setY, Integer::parseInt));
        while(!input("name", locationFrom::setName, str->str));
        route.setFrom(locationFrom);

//        consoleWorker.skip();
//        consoleWorker.print("Enter location to:");
        LocationTo locationTo = new LocationTo();
        while(!input("x", locationTo::setX, Long::parseLong));
        while(!input("y", locationTo::setY, Long::parseLong));
        while(!input("z", locationTo::setZ, Double::parseDouble));
        route.setTo(locationTo);

        while(!input("distance", route::setDistance, Long::parseLong));

        return route;
    }

    private <K> boolean input(String fieldName, Consumer<K> setter, Function<String, K> parser, String line) throws Throwable {
        try {
            if (line.equals("return")) throw new Throwable("stop using return");

            setter.accept(parser.apply(line));
            return true;
        } catch (Exception ex) {
//            consoleWorker.error(ex.getMessage());
            return false;
        }
    }

    private <K> boolean input(String fieldName, Consumer<K> setter, Function<String, K> parser) throws Throwable {
        try {
            String line = "";//consoleWorker.get(" - " + fieldName);
            if (line.equals("return")) throw new Throwable("stop using return");

            setter.accept(parser.apply(line));
            return true;
        } catch (Exception ex) {
//            consoleWorker.error(ex.getMessage());
            return false;
        }
    }
}
