package cache.helper;

import cache.impl.LRUCache;

import java.util.stream.IntStream;

public final class Task implements Runnable {
    private final LRUCache<Integer, String> lruCache;

    public Task(final LRUCache lruCache) {
        this.lruCache = lruCache;

    }

    @Override
    public void run() {
        IntStream.range(0, 10).forEach(key -> {
                    String value = "value" + key;
                    lruCache.put(key, value);
                    lruCache.get(key);
                    lruCache.remove(key);
                }
        );
    }
}
