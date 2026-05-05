package PD04.catalog;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class Catalog<T> {
    private final Map<Integer, T> items = new LinkedHashMap<>();

    public void add(int id, T element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        if (items.containsKey(id)) {
            throw new IllegalArgumentException("Element with id " + id + " already exists");
        }
        items.put(id, element);
    }
    public Optional<T> find(int id) {
        return Optional.ofNullable(items.get(id));
    }
    public boolean contains(int id) {
        return items.containsKey(id);
    }
    public int size() {
        return items.size();
    }
}
