package cache.helper;

public class TestDataHelper {
    public static String getResourceByKey(int key) {
        String value = "value";
        for (int i = 0; i < key; ++i) {
            value = value + i + "value";
        }
        return value;
    }
}
