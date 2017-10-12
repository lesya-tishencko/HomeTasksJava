package ru.spbau.java.tishencko;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by lesya on 09.10.2017.
 */
class Task<R> implements Runnable {
    private final Supplier<R> supplier;
    private Consumer<Task> queueAdder;
    private volatile R result = null;
    private volatile LightExecutionException exception = null;
    private boolean isReady = false;
    private List<Task> dependent = new ArrayList<>();

    public Task(Supplier<R> supplier, Consumer<Task> queueAdder) {
        this.supplier = supplier;
        this.queueAdder = queueAdder;
    }

    @Override
    public void run() {
        try {
            result = supplier.get();
        } catch (Throwable exc) {
            exception = new LightExecutionException();
        } finally {
            synchronized (this) {
                isReady = true;
                notifyAll();
            }
        }
    }

    private void waitForReady() throws InterruptedException {
        if (!isReady) {
            synchronized (this) {
                while (!isReady) {
                    wait();
                }
            }
        }
    }

    LightFuture<R> getFuture() {
        Task thisTask = this;
        return new LightFuture<R>() {
            @Override
            public boolean isReady() {
                return isReady;
            }

            @Override
            public R get() throws LightExecutionException, InterruptedException {
                waitForReady();
                if (exception != null) {
                    throw exception;
                }
                return result;
            }

            @Override
            public <T> LightFuture<T> thenApply(Function<? super R, ? extends T> function) {
                Task<T> dependentTask = new Task<>(new Supplier<T>() {
                    @Override
                    public T get() {
                        try {
                            waitForReady();
                        } catch (InterruptedException e) {
                            throw new InterruptedInArgumentException();
                        }

                        R arg = result;
                        return function.apply(arg);
                    }
                }, queueAdder);

                synchronized (thisTask) {
                    if (thisTask.isReady) {
                        queueAdder.accept(dependentTask);
                    } else {
                        dependent.add(dependentTask);
                    }
                }
                return dependentTask.getFuture();
            }
        };
    }

    public List<Task> getDependent() {
        return dependent;
    }

    private static class InterruptedInArgumentException extends RuntimeException {}
}
