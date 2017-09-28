import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Created by lesya on 27.09.2017.
 */
public class ThreadPoolImplTest {
    @Test
    public void checkCountOfThreads() throws Exception {
        ThreadPool pool = new ThreadPoolImpl(4);
        ReentrantLock lock = new ReentrantLock();
        final int[] index = {0};
        LightFuture<Boolean>[] futures = new LightFuture[4];
        for (int i = 0; i < 4; i++) {
            futures[i] = pool.push(() -> {
                lock.lock();
                index[0] += 1;
                System.out.println(Thread.currentThread().getName());
                lock.unlock();
                return true;
            });
        }
        pool.shutdown();
        for (int i = 0; i < 4; i++) {
            assertTrue(futures[i].get());
        }
        assertEquals(4, index[0]);
    }

    @Test
    public void simpleCheck() throws Exception {
        ThreadPool pool = new ThreadPoolImpl(2);
        LightFuture<Boolean> w1 = pool.push(()-> true);
        LightFuture<Boolean> w2 = pool.push(()-> false);
        pool.shutdown();
        assertFalse(w1.isReady());
        assertFalse(w2.isReady());
        assertTrue(w1.get());
        assertFalse(w2.get());
    }
}