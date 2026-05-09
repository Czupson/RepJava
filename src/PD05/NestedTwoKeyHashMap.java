package PD05;

import java.util.*;

public class NestedTwoKeyHashMap<K1, K2, V> implements TwoKeyMap<K1, K2, V> {
    private  final Map<K1, Map<K2, V>> data = new HashMap<>();
    private int size = 0;

    @Override
    public V put(K1 k1, K2 k2, V value) {
        Objects.requireNonNull(k1);
        Objects.requireNonNull(k2);
        Objects.requireNonNull(value);

        Map<K2, V> innerMap = data.computeIfAbsent(k1, k -> new HashMap<>());
        V old = innerMap.put(k2, value);
        if(old == null){
            size++;
        }
        return old;
    }
    @Override
    public V get(K1 k1, K2 k2) {
        Objects.requireNonNull(k1);
        Objects.requireNonNull(k2);
        Map<K2, V> innerMap = data.get(k1);
        if(innerMap == null){
            return null;
        }
        return innerMap.get(k2);
    }
    @Override
    public V remove(K1 k1, K2 k2) {
        Objects.requireNonNull(k1);
        Objects.requireNonNull(k2);
        Map<K2, V> innerMap = data.get(k1);
        if(innerMap == null){
            return null;
        }
        V removed = innerMap.remove(k2);
        if(removed != null){
            size--;
        }
        if(innerMap.isEmpty()){
            data.remove(k1);
        }
        return removed;
    }
    @Override
    public boolean containsKeys(K1 k1, K2 k2) {
        Objects.requireNonNull(k1);
        Objects.requireNonNull(k2);
        Map<K2, V> innerMap = data.get(k1);
        return innerMap != null && innerMap.containsKey(k2);
    }

    @Override
    public boolean containsValue(V value) {
        Objects.requireNonNull(value);
        for(Map<K2, V> innerMap : data.values()){
            if (innerMap.containsValue(value)){
                return true;
            }
        }
        return false;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    @Override
    public void clear() {
        data.clear();
        size = 0;
    }
    private static class SimpleEntry<K1, K2, V> implements Entry<K1, K2, V> {
        private final K1 key1;
        private final K2 key2;
        private V value;

        public SimpleEntry(K1 key1, K2 key2, V value) {
            this.key1 = key1;
            this.key2 = key2;
            this.value = value;
        }
        @Override
        public K1 getKey1() {
            return key1;
        }
        @Override
        public K2 getKey2() {
            return key2;
        }
        @Override
        public V getValue() {
            return value;
        }
        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
        @Override
        public String toString() {
            return key1 + ", " + key2 + " = " + value;
        }
    }
    @Override
    public Set<Entry<K1, K2, V>> entrySet(){
        Set<Entry<K1, K2, V>> result = new HashSet<>();

        for (Map.Entry<K1, Map<K2, V>> outer : data.entrySet()){
            K1 k1 = outer.getKey();
            for (Map.Entry<K2, V> inner : outer.getValue().entrySet()){
                result.add(
                        new SimpleEntry<>(
                                k1,
                                inner.getKey(),
                                inner.getValue())
                );
            }
        }
        return result;
    }
    @Override
    public Set<Pair<K1, K2>> keySet(){
        Set<Pair<K1, K2>> result = new HashSet<>();
        for (Map.Entry<K1, Map<K2, V>> outer : data.entrySet()){
            for (K2 k2 : outer.getValue().keySet()){
                result.add(
                        new Pair<>(
                                outer.getKey(),
                                k2
                        )
                );
            }
        }
        return result;
    }
    @Override
    public Collection<V> values() {
        List<V> result = new ArrayList<>();
        for (Map<K2, V> innerMap : data.values()){
            result.addAll(innerMap.values());
        }
        return result;
    }
    @Override
    public void putAll(TwoKeyMap<? extends K1,
            ? extends K2,
            ? extends V> other){
        for (Entry<? extends K1,
                ? extends K2,
                ? extends V> entry :other){
            put(
                    entry.getKey1(),
                    entry.getKey2(),
                    entry.getValue()
            );
        }
    }
    @Override
    public Map<K2, V> row(K1 k1){
        Objects.requireNonNull(k1);
        Map<K2, V> row = data.get(k1);
        if(row == null){
            return Collections.emptyMap();
        }
        return new HashMap<>(row);
    }
    @Override
    public Map<K1, V> column(K2 k2){
        Objects.requireNonNull(k2);
        Map<K1, V> result = new HashMap<>();
        for (Map.Entry<K1, Map<K2, V>> outer : data.entrySet()){
            if (outer.getValue().containsKey(k2)){
                result.put(
                        outer.getKey(),
                        outer.getValue().get(k2)
                );
            }
        }
        return result;
    }
    @Override
    public Iterator<Entry<K1, K2, V>> iterator(){
        return entrySet().iterator();
    }
}
