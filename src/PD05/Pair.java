package PD05;

import java.util.Objects;

public class Pair<K1, K2> {
    private final K1 key1;
    private final K2 key2;

    public Pair(K1 k1, K2 k2) {
        this.key1 = Objects.requireNonNull(k1);
        this.key2 = Objects.requireNonNull(k2);
    }
    public K1 getKey1() {
        return key1;
    }
    public K2 getKey2() {
        return key2;
    }
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Pair<?, ?> pair)) return false;
        return Objects.equals(key1, pair.key1) && Objects.equals(key2, pair.key2);
    }
    @Override
    public int hashCode() {
        return Objects.hash(key1, key2);
    }
    @Override
    public String toString() {
        return "(" + key1 + ", " + key2 + ")";
    }
}
