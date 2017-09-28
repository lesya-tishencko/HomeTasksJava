import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by lesya on 19.09.2017.
 */
public class LightFuture<X> {
    public LightFuture(Supplier<X> fun) {
        function = fun;
    }

    public boolean isReady() {
        return result != null || exception != null;
    }

    public X get() throws LightExecutionException {
        if (isReady()) {
            return result;
        }
        try {
            Thread task = new Thread(() -> {
                try {
                    result = function.get();
                } catch (Throwable exc) {
                    exception = new LightExecutionException();
                }
            });
            task.start();
            task.join();
        } catch (InterruptedException interrupt) {
            Thread.currentThread().interrupt();
        } finally {
            if (exception != null)
                throw exception;
            return result;
        }
    }

    public <Y> LightFuture<Y> thenApply(Function<? super X, ? extends Y> fun) {
        return new LightFuture<>(new Supplier<Y>() {
            @Override
            public Y get() {
                X arg = getArg();
                if (arg != null)
                    return fun.apply(getArg());
                else
                    return null;
            }

            private X getArg() {
                try {
                    return LightFuture.this.get();
                } catch (Throwable exc) {
                    return null;
                }
            }
        });
    }

    private X result = null;
    private LightExecutionException exception = null;
    private final Supplier<X> function;
}
