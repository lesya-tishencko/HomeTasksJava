package ru.spbau.java.tishencko;

import java.util.function.Supplier;

/**
 * Created by lesya on 23.09.2017.
 */
public interface ThreadPool {
    void shutdown();
    <R> LightFuture<R> push(Supplier<R> fun);
}
