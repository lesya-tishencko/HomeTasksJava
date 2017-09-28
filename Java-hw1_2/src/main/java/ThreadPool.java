import java.util.function.Supplier;

/**
 * Created by lesya on 23.09.2017.
 */
public interface ThreadPool {
    void shutdown();
    <X> LightFuture<X> push(Supplier<X> fun);
}
