import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by lesya on 09.03.2017.
 */
public class HashDictionaryTest {

    @Test
    public void putGeneral() throws Exception {
        HashDictionary hashMap = new HashDictionary();

        assertNull(hashMap.put("1", "apple"));
        assertNull(hashMap.put("2", "orange"));
        assertNull(hashMap.put("3", "banana"));
        assertNull(hashMap.put("4", "strawberry"));

        assertEquals("orange", hashMap.put("2", "pineapple"));
        assertEquals("pineapple", hashMap.put("2", "orange"));

        assertNull(hashMap.put("5", "pineapple"));
        assertEquals(5, hashMap.size());
    }

    @Test
    public void getGeneral() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        hashMap.put("1", "apple");
        hashMap.put("2", "orange");
        hashMap.put("3", "banana");
        hashMap.put("4", "strawberry");
        hashMap.put("5", "pineapple");

        assertEquals("strawberry", hashMap.get("4"));
        assertEquals("apple", hashMap.get("1"));
        assertNull(hashMap.get("100"));

        assertTrue(hashMap.contains("4"));
        assertFalse(hashMap.contains("0"));
        assertEquals(5, hashMap.size());
    }

    @Test
    public void removeGeneral() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        hashMap.put("1", "apple");
        hashMap.put("2", "orange");
        hashMap.put("3", "banana");
        hashMap.put("4", "strawberry");
        hashMap.put("5", "pineapple");

        assertEquals("apple", hashMap.remove("1"));
        assertNull(hashMap.remove("1"));
        assertEquals("pineapple", hashMap.remove("5"));
        assertNull(hashMap.remove("0"));
        assertEquals(3, hashMap.size());

        assertNull(hashMap.put("5", "grapes"));
        assertNull(hashMap.put("1", "cherry"));
        assertEquals(5, hashMap.size());
    }

    @Test
    public void testRehashing() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        for (int i = 0; i < 200; i++) {
            assertNull(hashMap.put(String.valueOf(i), ":" + String.valueOf(i)));
            assertEquals(i + 1, hashMap.size());
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(":" + String.valueOf(i), hashMap.remove(String.valueOf(i)));
            assertEquals(200 - i - 1, hashMap.size());
        }

        assertEquals(100, hashMap.size());
        hashMap.clear();
        for (int i = 100; i < 200; i++)
            assertFalse(hashMap.contains(String.valueOf(i)));
        assertEquals(0, hashMap.size());
    }
}
