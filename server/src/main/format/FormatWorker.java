package main.format;

import main.models.Route;
import main.models.User;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FormatWorker <K> {
    List<K> readFile();

    void writeFile(Collection<Route> values);

    void removeById(Long id);

    Optional<User> getUser(String login, String password);

    void addUser(String login, String password);
}
