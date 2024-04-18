package com.projects.countrycode.component;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

 class CacheTest {

    private Cache cache;

    @BeforeEach
    public void setUp() {
        cache = new Cache();
    }

    @Test
    void testPutAndGetFromCache() {
        String key = "key";
        Object value = new Object();

        cache.putCache(key, value);
        Object retrievedValue = cache.getCache(key);

        assertNotNull(retrievedValue);
        assertEquals(value, retrievedValue);
    }

    @Test
    void testContainsKey() {
        String key = "key";
        Object value = new Object();
        cache.putCache(key, value);

        boolean contains = cache.containsKey(key);

        assertTrue(contains);
    }

    @Test
    void testRemoveFromCache() {

        String key = "key";
        Object value = new Object();
        cache.putCache(key, value);

        cache.remove(key);
        boolean contains = cache.containsKey(key);

        assertFalse(contains);
    }

    @Test
    void testCacheSizeLimit() {

        for (int i = 1; i <= 20; i++) {
            cache.putCache("key" + i, new Object());
        }

        boolean containsKey1 = cache.containsKey("key1");
        boolean containsKey16 = cache.containsKey("key16");

        assertFalse(containsKey1);
        assertTrue(containsKey16);
    }
}
