package main.collection;

import main.Main;
import main.format.FileManager;
import main.models.Route;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionManager {
    private UserCollection collection = null;
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private CollectionManager() {}

    private static CollectionManager instance = null;

    public static CollectionManager getInstance() {
        if (instance == null) instance = new CollectionManager();
        return instance;
    }

    public UserCollection getCollection() {
            if (collection == null) load();
            return collection;
    }

    public int getFreeId() {
        readWriteLock.readLock().lock();
        try {
            return collection.stream()
                    .mapToInt(Route::getId)
                    .max()
                    .orElse(0) + 1;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Route getMax() {
        readWriteLock.readLock().lock();
        try {
            return collection.stream()
                    .max(Route::compareTo)
                    .orElse(null);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public Route getMin() {
        readWriteLock.readLock().lock();
        try {
            return collection.stream()
                    .min(Route::compareTo)
                    .orElse(null);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }


    public void save() {
        readWriteLock.writeLock().lock();
        try {
            FileManager.saveCollection(collection);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public UserCollection load() {
        readWriteLock.writeLock().lock();
        try {
            collection = FileManager.loadCollection();
            return collection;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
