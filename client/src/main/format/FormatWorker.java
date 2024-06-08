package main.format;

import main.models.Route;

import java.util.Collection;
import java.util.List;

public interface FormatWorker<K> {
    List<K> readFile();
    List<K> readString(String str);

    void writeFile(Collection<Route> values);
    String writeString(List<K> values);

    void removeById(Long id);
}
