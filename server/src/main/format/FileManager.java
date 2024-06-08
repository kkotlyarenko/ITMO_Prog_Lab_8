package main.format;

import main.collection.UserCollection;
import main.models.Route;


public class FileManager {
    private static final FormatWorker<Route> formatWorker = new PostgresFormatWorker("jdbc:postgresql://localhost:5454/postgres", "user", "password");

    public static UserCollection loadCollection() {
        UserCollection collection = new UserCollection();
        formatWorker.readFile().forEach(collection::add);
        return collection;
    }

    public static void saveCollection(UserCollection collection) {
        formatWorker.writeFile(collection);
    }
}
