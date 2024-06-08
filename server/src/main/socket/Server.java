package main.socket;

import main.format.PostgresFormatWorker;
import main.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.Main;
import main.collection.CollectionManager;
import main.command.handler.CommandHandler;
import main.console.Request;
import main.console.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.*;
import java.util.logging.Handler;


public class Server implements Runnable, Serializable {
    Logger log = LoggerFactory.getLogger(this.getClass());
    // db service
    PostgresFormatWorker userService;

    // chat handler
    CommandHandler handler;
    HashMap<String, Integer> commandsUsage = new HashMap<>();

    ForkJoinPool readPool = new ForkJoinPool();
    ForkJoinPool processPool = new ForkJoinPool();

    public Server(CommandHandler handler) {
        this.userService = new PostgresFormatWorker("jdbc:postgresql://localhost:5454/postgres", "user", "password");
        this.handler = handler;
        init();
    }
    /**
     * Initialize server
     */
    private void init() {
        // try to load collection
        CollectionManager.getInstance().getCollection();
    }

    /**
     * Run server
     */
    @Override
    public void run() {
        System.out.println("Server started");
        try (ServerSocket server = new ServerSocket(8080)) {
            log.info("server started on port {}", 8080);

            while (!server.isClosed())
                handleSession(server.accept());
        } catch (IOException e) {
            log.error("Server exception: {}", e.getMessage());
        }
    }

    private void handleSession(Socket client) {
        readPool.submit(() -> {
            try (Socket autoCloseableClient = client;
                 ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream())) {

                log.info("client connected: {}", client.getInetAddress());

                while (!client.isClosed()) {
                    Request request = (Request) objectInputStream.readObject();
                    log.info("request from client ({}): {}", client.getInetAddress(), request.toString());

                    if (request.getLogin() == null || request.getPassword() == null) {
                        objectOutputStream.writeObject(new Response("Enter username/password!"));
                        objectOutputStream.flush();
                        continue;
                    }

                    Optional<User> optionalUser = authorize(request.getLogin(), request.getPassword());

                    if (optionalUser.isEmpty() && userService.existByUsername(request.getLogin())) {
                        objectOutputStream.writeObject(new Response("Incorrect username/password"));
                        objectOutputStream.flush();
                        continue;
                    }

                    if (optionalUser.isEmpty()) {
                        userService.addUser(request.getLogin(), request.getPassword());
                        objectOutputStream.writeObject(new Response("User created"));
                        continue;
                    }

                    if (request.getCommand().equalsIgnoreCase("CONNECT")) {
                        log.info("connect request from client({})", client.getInetAddress());
                        Response connectResponse = new Response("CONNECTED");
                        connectResponse.setCommands(handler.getCommands());

                        objectOutputStream.writeObject(connectResponse);
                        continue;
                    }

                    Response response = handleClient(request);

                    log.info("response for client ({}): {}", client.getInetAddress(), response.toString());

                    new Thread(() -> {
                        try {
                            System.out.println(response.getRoutes());
                            objectOutputStream.writeObject(response);
                            objectOutputStream.flush();
                        } catch (IOException e) {
                            log.warn(e.getMessage());
                        }
                    }).start();
                }

                log.info("client {} disconnected", client.getInetAddress());
            } catch (Exception e) {
                log.error("Error handling client {}: {}", client.getInetAddress(), e.getMessage());
            }
        });
    }

    private Optional<User> authorize(String username, String password) {
        return userService.getUser(username, password);
    }

    /**
     * Handle client request
     * @param request client request
     * @return response
     */
    private Response handleClient(Request request) throws ExecutionException, InterruptedException, TimeoutException {
        return processPool.submit(() -> handler.handle(request)).get(10, TimeUnit.SECONDS);
    }
}
