package PD05;

import java.util.Collection;
import java.util.Set;

public interface TwoKeyMap<K1, K2, V> extends Iterable<TwoKeyMap.Entry<K1, K2, V>> {
    V put (K1 k1, K2 k2, V value);
    V get (K1 k1, K2 k2);
    V remove (K1 k1, K2 k2);
    boolean containsKeys (K1 k1, K2 k2);
    boolean containsValue (V value);

    int size ();
    boolean isEmpty ();
    Set<Entry<K1, K2, V>> entrySet();
    Set<Pair<K1, K2>> keySet();
    Collection<V> values();

    void putAll(TwoKeyMap<? extends K1, ? extends K2, ? extends V> other);
    void clear();

    java.util.Map<K2, V> row (K1 k1);
    java.util.Map<K1, V> column (K2 k2);

    interface Entry<K1, K2, V> {
        K1 getKey1();
        K2 getKey2();
        V getValue();
        V setValue(V value);
    }
}
