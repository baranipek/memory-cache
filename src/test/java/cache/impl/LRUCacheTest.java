package cache.impl;

import cache.helper.DataTestHolder;
import cache.helper.Task;
import cache.helper.TestData;
import cache.helper.TestDataHelper;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.MILLIS;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

public class LRUCacheTest {

    @Test
    public void cacheDataWithKey_PutCache_GetTheObjectSuccessfully() {
        //given
        TestData testData = new TestData();
        testData.setValue("expectedValue");
        DataTestHolder testHolder = new DataTestHolder(testData);
        //when
        LRUCache<String, TestData> cache = new LRUCache(testHolder, 100);
        cache.put("myKey", testData);

        //then
        assertEquals(cache.get("myKey"), testData);
    }


    @Test
    public void cacheDataWithSameKey_PutCache_GetTheSameObjectSuccessfully() {
        //given
        TestData testData = new TestData();
        testData.setValue("expectedValue");
        DataTestHolder testHolder = new DataTestHolder(testData);
        //when
        LRUCache<String, TestData> cache = new LRUCache(testHolder, 100);
        cache.put("myKey", testData);
        cache.put("myKey", testData);

        //then
        assertEquals(cache.get("myKey"), testData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cacheDataWithoutKey_PutCache_IllegalExceptionIsThrown() {
        //given
        TestData testData = new TestData();
        testData.setValue("expectedValue");
        DataTestHolder testHolder = new DataTestHolder(testData);
        //when
        LRUCache<String, TestData> cache = new LRUCache(testHolder, 100);
        cache.put(null,testData);
    }

    @Test
    public void multipleKeyValuesInTheCache_PutGetCache_AllElementsAreReturned() {
        //given
        DataTestHolder testHolder = new DataTestHolder(new TestData());
        LRUCache<Integer, TestData> cache = new LRUCache(testHolder, 100);
        for (int i = 0; i < 5; i++) {
            TestData testData = new TestData();
            String value = "value" + i;
            testData.setValue(value);
            cache.put(i, testData);
        }
        //when && then
        TestData expectedTestData = new TestData();
        expectedTestData.setValue("value1");
        assertNotNull(cache);
        assertEquals(cache.getSize(), 5);
        assertEquals(cache.get(1).getValue(), expectedTestData.getValue());
    }

    @Test
    public void multipleKeyValuesInTheCache_PutGetCacheListSizeExceed_OldElementsAreRemoved() {
        //given
        DataTestHolder testHolder = new DataTestHolder(new String());
        LRUCache<Integer, String> cache = new LRUCache(testHolder, 5);
        for (int i = 0; i < 5; i++) {
            String value = "value" + i;
            cache.put(i, value);
        }
        //when
        cache.get(1);
        cache.get(2);
        cache.put(6, "value6");
        cache.put(7, "value6");

        //then
        assertNotNull(cache);
        assertEquals(cache.getSize(), 5);
        assertNull(cache.get(0));
        assertNull(cache.get(3));
        assertNotNull(cache.get(6));
    }

    @Test
    public void multipleKeyValueResources_GetResourceWithAndWithoutCache_CacheHitIsFaster() {
        //given
        DataTestHolder testHolder = new DataTestHolder(new String());
        LRUCache<Integer, String> cache = new LRUCache(testHolder, 10000);
        for (int j = 0; j < 1000; ++j) {
            for (int i = 0; i < 100; ++i) {
                String value = "value" + i;
                cache.put(i, value);
            }
        }

        //when
        LocalDateTime beforeCacheHit = LocalDateTime.now();
        for (int j = 0; j < 1000; ++j) {
            for (int i = 0; i < 100; ++i) {
                cache.get(i);
            }
        }
        LocalDateTime afterCacheHit = LocalDateTime.now();

        long cacheTimeCalculation  = MILLIS.between(beforeCacheHit,afterCacheHit);
        LocalDateTime beforeWithoutCacheHit = LocalDateTime.now();
        for (int j = 0; j < 1000; ++j) {
            for (int i = 0; i < 100; ++i) {
                TestDataHelper.getResourceByKey(i);
            }
        }
        LocalDateTime afterWithoutCacheHit = LocalDateTime.now();

        long withoutCacheTimeCalculation = MILLIS.between(beforeWithoutCacheHit,afterWithoutCacheHit);
        // then
        assertTrue(withoutCacheTimeCalculation > cacheTimeCalculation);
    }

    @Test
    public void multipleThreadUpdateCache_GetAndPutCache_CacheAreManipulatedSynchronously() {
        //given
        boolean hasConcurrentException = false;
        DataTestHolder testHolder = new DataTestHolder(new String());
        LRUCache<Integer, String> cache = new LRUCache(testHolder, 10000);

        try {
            IntStream.range(0, 5).forEach(counter -> new Thread(new Task(cache)).start());

        } catch (ConcurrentModificationException cme) {
            hasConcurrentException = true;
        }

        assertFalse(hasConcurrentException);
    }

}