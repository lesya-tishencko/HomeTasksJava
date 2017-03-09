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

        assertEquals(hashMap.put("2", "pineapple"), "orange");
        assertEquals(hashMap.put("2", "orange"), "pineapple");

        assertNull(hashMap.put("5", "pineapple"));
        assertEquals(hashMap.size(), 5);
    }

    @Test
    public void getGeneral() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        hashMap.put("1", "apple");
        hashMap.put("2", "orange");
        hashMap.put("3", "banana");
        hashMap.put("4", "strawberry");
        hashMap.put("5", "pineapple");

        assertEquals(hashMap.get("4"), "strawberry");
        assertEquals(hashMap.get("1"), "apple");
        assertNull(hashMap.get("100"));

        assertTrue(hashMap.contains("4"));
        assertFalse(hashMap.contains("0"));
        assertEquals(hashMap.size(), 5);
    }

    @Test
    public void removeGeneral() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        hashMap.put("1", "apple");
        hashMap.put("2", "orange");
        hashMap.put("3", "banana");
        hashMap.put("4", "strawberry");
        hashMap.put("5", "pineapple");

        assertEquals(hashMap.remove("1"), "apple");
        assertNull(hashMap.remove("1"));
        assertEquals(hashMap.remove("5"), "pineapple");
        assertNull(hashMap.remove("0"));
        assertEquals(hashMap.size(), 3);

        assertNull(hashMap.put("5", "grapes"));
        assertNull(hashMap.put("1", "cherry"));
        assertEquals(hashMap.size(), 5);
    }

    @Test
    public void testRehashing() throws Exception {
        HashDictionary hashMap = new HashDictionary();
        for (int i = 0; i < 200; i++) {
            assertNull(hashMap.put(String.valueOf(i), ":" + String.valueOf(i)));
            assertEquals(hashMap.size(), i + 1);
        }

        for (int i = 0; i < 100; i++) {
            assertEquals(hashMap.remove(String.valueOf(i)), ":" + String.valueOf(i));
            assertEquals(hashMap.size(), 200 - i - 1);
        }

        assertEquals(hashMap.size(), 100);
        hashMap.clear();
        for (int i = 100; i < 200; i++)
            assertFalse(hashMap.contains(String.valueOf(i)));
        assertEquals(hashMap.size(), 0);
    }
}
