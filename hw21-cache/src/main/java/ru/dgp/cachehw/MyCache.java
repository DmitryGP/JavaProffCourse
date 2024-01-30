package ru.dgp.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> map;
    private final List<HwListener<K, V>> listeners;

    public MyCache(int capacity) {
        map = WeakHashMap.newWeakHashMap(capacity);
        listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
        listeners.stream().forEach(l -> l.notify(key, value, "PUT"));
    }

    @Override
    public void remove(K key) {
        var value = map.get(key);

        if (value != null) {
            map.remove(key);
            listeners.stream().forEach(l -> l.notify(key, value, "REMOVE"));
        }
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
