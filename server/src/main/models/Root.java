package main.models;

import main.collection.UserCollection;

public class Root {
    private UserCollection routes;

    public UserCollection getRoutes() {
        return routes;
    }

    public void setRoutes(UserCollection routes) {
        this.routes = routes;
    }

    public Root(UserCollection routes) {
        this.routes = routes;
    }
}
