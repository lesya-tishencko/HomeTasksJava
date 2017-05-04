import org.junit.Rule;
import org.junit.Test;

/**
 * Created by lesya on 04.05.2017.
 */
public class MemoryLeakLimitTest {
    private static int[] bigArray;

    @Rule
    public final MemoryLeakLimit leakLimit = new MemoryLeakLimit();

    @Test
    public void testLeak1() {
        leakLimit.limit(1);
        bigArray = new int[1024 * 1024];
    }

    @Test
    public void testLeak10() {
        leakLimit.limit(10);
        bigArray = new int[10 * 1024 * 1024];
    }
}