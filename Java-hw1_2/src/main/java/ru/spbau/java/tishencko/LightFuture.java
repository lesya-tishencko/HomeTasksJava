package ru.spbau.java.tishencko;

import java.util.function.Function;

/**
 * Created by lesya on 19.09.2017.
 */
public interface LightFuture<R> {
    boolean isReady();
    R get() throws LightExecutionException, InterruptedException;
    <T> LightFuture<T> thenApply(Function<? super R, ? extends T> function);
}
