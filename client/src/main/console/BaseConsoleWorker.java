package main.console;

import java.util.Date;
import java.util.Scanner;

public class BaseConsoleWorker extends ConsoleWorker{
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void error(String text) {
        System.err.println("Error: " + text);
    }

    @Override
    public String get(String label){
        final Date currDate = new Date();
        System.out.print(String.format(label, currDate, currDate, currDate) + ": ");
        try {
            return sc.nextLine();
        } catch (Exception ex) {
            System.exit(0);
            return "";
        }
    }
}
