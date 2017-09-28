import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Created by lesya on 23.09.2017.
 */
public class ThreadPoolImpl implements ThreadPool {
    ThreadPoolImpl(int n) {
        countOfNums = n;
        pool = new Thread[countOfNums];
        for (int i = 0; i < countOfNums; i++) {
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
    public <X> LightFuture<X> push(Supplier<X> fun) {
        LightFuture<X> current = new LightFuture(fun);
        synchronized(queue) {
            queue.addLast(current);
            queue.notify();
        }
        return current;
    }

    private class PoolWorker implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                LightFuture future;

                while (true) {
                    synchronized (queue) {
                        while (queue.isEmpty()) {
                            try {
                                queue.wait();
                            } catch (InterruptedException ignored) {
                            }
                        }

                        future = queue.removeFirst();
                    }

                    try {
                        future.get();
                    } catch (Throwable exc) {
                        // You might want to log something here
                    }
                }
            }
        }
    }

    private int countOfNums;
    private Thread[] pool;
    private LinkedList<LightFuture> queue = new LinkedList();
}
