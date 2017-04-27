package sp;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;

public class SecondPartTasksTest {

    @Test
    public void testFindQuotes() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile1 = new File(classLoader.getResource("exmp1.txt").getFile());
        File testFile2 = new File(classLoader.getResource("exmp2.txt").getFile());
        File testFile3 = new File(classLoader.getResource("exmp3.txt").getFile());
        List<String> paths = new ArrayList<String>(Arrays.asList(testFile1.getAbsolutePath(), testFile2.getAbsolutePath(), testFile3.getAbsolutePath()));
        assertEquals(Arrays.asList("blueberry", "strawberry", "blueberry", "strawberry", "blackberry"), SecondPartTasks.findQuotes(paths, "berry"));
        assertEquals(Arrays.asList("orange", "orange sweater", "orange juice"), SecondPartTasks.findQuotes(paths, "orange"));
    }

    @Test
    public void testPiDividedBy4() {
        double probability = SecondPartTasks.piDividedBy4();
        assertTrue(Math.abs(probability - Math.PI / 4) < 0.001);
    }

    @Test
    public void testFindPrinter() {
        String[] books = {"winter", "spring", "summer", "warm", "flowers", "the sun"};
        String[] auth = {"Auth1", "Auth2", "Auth3"};
        Map<String, List<String>> map = new HashMap<String, List<String>>(){{put("Auth1", Arrays.asList(books[0], books[2]));
                                                                             put("Auth2", Arrays.asList(books[1], books[3], books[4]));
                                                                             put("Auth3", Arrays.asList(books[1], books[5]));}};
        assertEquals("Auth2", SecondPartTasks.findPrinter(map));
    }

    @Test
    public void testCalculateGlobalOrder() {
        List<Map<String, Integer>> orders = Arrays.asList(new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>());
        orders.get(0).put("A", 1);
        orders.get(0).put("B", 5);
        orders.get(1).put("A", 8);
        orders.get(1).put("B", 3);
        orders.get(1).put("C", 10);
        orders.get(2).put("B", 4);
        orders.get(2).put("C", 1);
        Map<String, Integer> map = new HashMap<String, Integer>(){{put("A", 9); put("B", 12); put("C", 11);}};
        assertEquals(map, SecondPartTasks.calculateGlobalOrder(orders));
    }
}