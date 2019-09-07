package cache.impl;

import cache.Cache;
import domain.Resource;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache<K, V> implements Cache<K, V> {
    private Resource<V> resource;
    private final Map<K, V> cache;

    /**
     * Constructs a new cache.impl.LRUCache source and capacity
     *
     * @param resource    the source to be queried in order to satisfy request
     * @param maxCapacity maximum capacity of the cache, which when reached will result in the LRU
     *                    value in the cache being evicted
     */
    public LRUCache(final Resource<V> resource, final int maxCapacity) {
        this.resource = resource;
        this.cache = new LinkedHashMap<K, V>(maxCapacity, 0.75F, true) {
            private static final long serialVersionUID = -342342424242342367L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxCapacity;
            }
        };

    }

    /**
     * Put the entry into key
     *
     * @param key   key of the cache
     * @param value value of the cache
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be empty");
        }
        synchronized (cache) {
            cache.put(key, value);
        }
    }

    /**
     * Remove the entry from  cache
     *
     * @param key key of the cache
     */
    public void remove(K key) {
        synchronized (cache) {
            cache.remove(key);
        }
    }

    public int getSize() {
        return cache.size();
    }

    /**
     * Get the entry from  cache by key
     *
     * @param key key of the cache
     */
    public V get(K key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }
}
