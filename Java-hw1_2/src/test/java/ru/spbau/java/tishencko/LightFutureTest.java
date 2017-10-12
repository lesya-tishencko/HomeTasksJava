package ru.spbau.java.tishencko;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Created by lesya on 27.09.2017.
 */
public class LightFutureTest {
    private ThreadPool threadPool;
    private Supplier<List<Integer>> fibSupplier;

    @Before
    public void SetUp() {
        threadPool = new ThreadPoolImpl(4);
        fibSupplier = new Supplier<List<Integer>>() {
            final List<Integer> fibonacci = new ArrayList<>();
            @Override
            public List<Integer> get() {
                fibonacci.add(1);
                fibonacci.add(1);
                for (int i = 2; i < 10; ++i) {
                    fibonacci.add(fibonacci.get(i - 2) + fibonacci.get(i - 1));
                }
                return fibonacci;
            }
        };
    }

    @Test
    public void isReady() throws Exception {
        LightFuture<List<Integer>> future = threadPool.push(fibSupplier);
        assertEquals(future.get(), Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21, 34, 55));
        assertTrue(future.isReady());
    }

    @Test(expected = LightExecutionException.class)
    public void get() throws Exception {
        Supplier<Integer> exceptionSupplier = ()-> {
            throw new ArithmeticException();
        };
        LightFuture<Integer> futureWithException = threadPool.push(exceptionSupplier);
        futureWithException.get();
    }

    @Test
    public void thenApply() throws Exception {
        LightFuture<List<Integer>> future = threadPool.push(fibSupplier);
        Function<List<Integer>, Integer> function = integers -> integers.stream().reduce((s1, s2) -> s1 + s2).get();
        LightFuture<Integer> future2 = future.thenApply(function);
        assertEquals(function.apply(future.get()), future2.get());
        LightFuture<Integer> future3 = future.thenApply(function);
        assertEquals(future2.get(), future3.get());
    }

    @Test
    public void testForNull() throws Exception {
        Supplier<Integer> nullSupplier = () -> { return null; };
        LightFuture<Integer> future = threadPool.push(nullSupplier);
        assertNull(future.get());
        assertTrue(future.isReady());
    }
}