package ru.spbau.java.tishencko;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Created by lesya on 23.09.2017.
 */
public class ThreadPoolImpl implements ThreadPool {
    private final Thread[] pool;
    private final Queue<Task> queue = new LinkedList<>();

    ThreadPoolImpl(int n) {
        pool = new Thread[n];
        for (int i = 0; i < n; i++) {
            pool[i] = new Thread(new PoolWorker());
            pool[i].start();
        }
    }

    @Override
    public void shutdown() {
        for (Thread worker : pool) {
            worker.interrupt();
        }
    }

    @Override
    public <R> LightFuture<R> push(Supplier<R> supplier) {
        Task<R> current = new Task<>(supplier, this::submitTask);
        submitTask(current);
        return current.getFuture();
    }

    private void submitTask(Task task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    private class PoolWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                Task task;
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ignored) {
                        }
                    }
                    task = queue.remove();
                }
                task.run();
                List<Task> dependentTasks = task.getDependent();
                for (Task dependentTask : dependentTasks) {
                    submitTask(dependentTask);
                }
            }
        }
    }
}
