package main.collection;

import main.Main;
import main.format.FileManager;
import main.models.Route;

import java.nio.file.Path;

public class CollectionManager {
    private UserCollection collection = null;
    private CollectionManager() {}

    private static CollectionManager instance = null;

    public static CollectionManager getInstance() {
        if (instance == null) instance = new CollectionManager();
        return instance;
    }

    public UserCollection getCollection() {
        if (collection == null) load(Main.ROOT_FILE);
        return collection;
    }

    public int getFreeId() {
        try{
            return collection.stream()
                    .mapToInt(Route::getId)
                    .max()
                    .orElse(0) + 1;
        } catch (NullPointerException e) {
            return 1;
        }
    }

    public Route getMax() {
        return collection.stream()
                .max(Route::compareTo)
                .orElse(null);
    }

    public Route getMin() {
        return collection.stream()
                .min(Route::compareTo)
                .orElse(null);
    }


    public void save() {
        FileManager.saveCollection(collection);
    }

    public UserCollection load() {
        return load(Main.ROOT_FILE);
    }

    public UserCollection load(Path filePath) {
        collection = FileManager.loadCollection(filePath);
        return collection;
    }


}
