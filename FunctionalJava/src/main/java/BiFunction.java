/**
 * Created by lesya on 06.04.2017.
 */
public abstract class BiFunction<T, F, R> {
    public abstract R apply(T arg1, F arg2);

    public <V> BiFunction<T, F, V> compose(Function<? super R, ? extends V> after) {
        return new BiFunction<T, F, V>() {
            @Override
            public V apply(T arg1, F arg2) {
                return after.apply(BiFunction.this.apply(arg1, arg2));
            }
        };
    }

    public Function<F, R> bind1(T arg) {
        return new Function<F, R>() {
            @Override
            public R apply(F from) {
                return BiFunction.this.apply(arg, from);
            }
        };
    }

    public Function<T, R> bind2(F arg) {
        return new Function<T, R>() {
            @Override
            public R apply(T from) {
                return BiFunction.this.apply(from, arg);
            }
        };
    }

    public Function<T, Function<F, R>> carry() {
        return new Function<T, Function<F, R>>() {
            @Override
            public Function<F, R> apply(T from) {
                return BiFunction.this.bind1(from);
            }
        };
    }
}
