import java.util.Objects;

/**
 * Created by lesya on 06.04.2017.
 */
public abstract class Predicate<T> extends Function<T, Boolean> {
    public Predicate<T> not() {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T from) {
                return !Predicate.this.apply(from);
            }
        };
    }

    public Predicate<T> and(Predicate<? super T> other) {
        return new Predicate<T>() {
            @Override
            public Boolean apply(T from) {
                return Predicate.this.apply(from) && other.apply(from);
            }
        };
    }

    public Predicate<T> or(Predicate<? super T> other) {
        return  new Predicate<T>() {
            @Override
            public Boolean apply(T from) {
                return Predicate.this.apply(from) || other.apply(from);
            }
        };
    }

    public static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        @Override
        public Boolean apply(Object from) {
            return Boolean.TRUE;
        }
    };

    public static final Predicate<Object> ALWAYS_FALSE = ALWAYS_TRUE.not();
}
