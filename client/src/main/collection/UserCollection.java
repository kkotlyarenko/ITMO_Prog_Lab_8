package main.collection;

import main.models.Route;

import java.util.LinkedList;

public class UserCollection extends LinkedList<Route> {
    private final java.time.LocalDateTime initializedDate = java.time.LocalDateTime.now();

    public java.time.LocalDateTime getInitializedDate() {
        return initializedDate;
    }

    @Override
    public boolean add(Route element) {
        super.add(element);
        return false;
    }

}
