import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Created by lesya on 27.09.2017.
 */
public class LightFutureTest {
    @Test
    public void isReady() throws Exception {
        LightFuture<List<Integer>> future = new LightFuture<>(new Supplier<List<Integer>>() {
            List<Integer> fibonacci = new ArrayList<>();
            @Override
            public List<Integer> get() {
                fibonacci.add(1);
                fibonacci.add(1);
                for (int i = 2; i < 100; ++i) {
                    fibonacci.add(fibonacci.get(i - 2) + fibonacci.get(i - 1));
                }
                return fibonacci;
            }
        });
        assertFalse(future.isReady());
        future.get();
        assertTrue(future.isReady());
    }

    @Test(expected = LightExecutionException.class)
    public void get() throws Exception {
        LightFuture<Integer> futureWithException = new LightFuture<>(()->{return 3 / 0;});
        futureWithException.get();
    }

    @Test
    public void thenApply() throws Exception {
        LightFuture<List<Integer>> future = new LightFuture<>(new Supplier<List<Integer>>() {
            List<Integer> fibonacci = new ArrayList<>();
            @Override
            public List<Integer> get() {
                fibonacci.add(1);
                fibonacci.add(1);
                for (int i = 2; i < 100; ++i) {
                    fibonacci.add(fibonacci.get(i - 2) + fibonacci.get(i - 1));
                }
                return fibonacci;
            }
        });
        Function<List<Integer>, Integer> function = new Function<List<Integer>, Integer>() {
            @Override
            public Integer apply(List<Integer> integers) {
                return integers.stream().reduce((s1, s2) -> s1 + s2).get();
            }
        };
        LightFuture<Integer> future2 = future.thenApply(function);
        assertFalse(future.isReady());
        assertFalse(future2.isReady());
        assertEquals(future.get().stream().reduce((s1, s2) -> s1 + s2).get(), future2.get());
        LightFuture<Integer> future3 = future.thenApply(function);
        assertEquals(future2.get(), future3.get());
    }

}