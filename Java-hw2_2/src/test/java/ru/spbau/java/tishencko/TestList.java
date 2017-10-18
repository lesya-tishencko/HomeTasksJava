package ru.spbau.java.tishencko;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * Created by lesya on 18.10.2017.
 */
public class TestList {
    private LockFreeList<Integer> list;
    private ArrayList<Thread> threads;
    private final int threadCount = 1024;

    @Before
    public void initialize() {
        list = new List<>();
        threads = new ArrayList<>();
    }
    @Test
    public void isEmpty() throws Exception {
        assertTrue(list.isEmpty());
        list.append(100);
        list.append(100);
        list.append(20);
        assertFalse(list.isEmpty());
        assertTrue(list.remove(100));
        assertFalse(list.isEmpty());
        assertTrue(list.remove(100));
        assertTrue(list.remove(20));
        assertTrue(list.isEmpty());
    }

    @Test
    public void append() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    barrier.await();
                } catch (Throwable ignored) {
                }
                list.append(finalI);
            }));
            threads.get(i).start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        for (int i = 0; i < threadCount; i++) {
            assertTrue(list.contains(i));
        }
    }

    @Test
    public void remove() throws Exception {
        for (int i = 0; i < threadCount; i++)
            list.append(i);

        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger countOfDeleting = new AtomicInteger(0);
        for (int i = 0; i < threadCount; ++i) {
            int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    barrier.await();
                } catch (Throwable ignored) {
                }
                if (list.remove(finalI))
                    countOfDeleting.incrementAndGet();
            }));
            threads.get(i).start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(threadCount, countOfDeleting.get());
        for (int i = 0; i < threadCount; i++) {
            assertFalse(list.contains(i));
        }
        assertTrue(list.isEmpty());
    }

    @Test
    public void contains() throws Exception {
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        for (int i = 0; i < threadCount; ++i) {
            threads.add(new Thread(() -> {
                try {
                    barrier.await();
                } catch (Throwable ignored) {
                }
                list.append(1024);
            }));
            threads.get(i).start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertTrue(list.contains(1024));
        for (int i = 0; i < threadCount - 1; i++)
            assertTrue(list.remove(1024));
        assertTrue(list.contains(1024));
        assertTrue(list.remove(1024));
        assertFalse(list.contains(1024));
        assertTrue(list.isEmpty());
    }
}