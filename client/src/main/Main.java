package main;

import main.command.Command;
import main.command.list.*;
import main.command.handler.CommandHandler;
import main.console.ConsoleWorker;
import main.console.BaseConsoleWorker;
import main.socket.Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class Main {
    public static Path ROOT_FILE;

    public static void main(String[] args) throws InterruptedException{
        Client client = new Client();

        new Thread(client).start();

    }
}
