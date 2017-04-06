/**
 * Created by lesya on 06.04.2017.
 */
public abstract class Function<T, R> {
    public abstract R apply(T from);

    public <V> Function<T,V> compose(Function<? super R, ? extends V> after) {
        return new Function<T,V>() {
            @Override
            public V apply(T from) {
                return after.apply(Function.this.apply(from));
            }
        };
    }
}
