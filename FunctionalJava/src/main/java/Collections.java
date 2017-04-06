import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by lesya on 06.04.2017.
 */
public class Collections {
    public static <F, T> Iterable<T> map(Iterable<F> from, Function<? super F, ? extends T> f) {
        ArrayList<T> result = new ArrayList<T>();
        Iterator<F> it = from.iterator();
        while (it.hasNext()) {
            result.add(f.apply(it.next()));
        }
        return result;
    }

    public static <T> Iterable<T> filter(Iterable<T> from, Predicate<? super T> p) {
        ArrayList<T> result = new ArrayList<T>();
        Iterator<T> it = from.iterator();
        while (it.hasNext()) {
            T elem = it.next();
            if (p.apply(elem))
                result.add(elem);
        }
        return result;
    }

    public static <T> Iterable<T> takeWhile(Iterable<T> from, Predicate<? super T> p) {
        ArrayList<T> result = new ArrayList<T>();
        Iterator<T> it = from.iterator();
        while (it.hasNext()) {
            T elem = it.next();
            if (!p.apply(elem))
                break;
            result.add(elem);
        }
        return result;
    }

    public static <T> Iterable<T> takeUnless(Iterable<T> from, Predicate<? super T> p) {
        return takeWhile(from, p.not());
    }

    public static <T, R> T foldl(Iterable<R> from, T acc, BiFunction<? super T,? super R,? extends T> biFun) {
        Iterator<R> it = from.iterator();
        T result = acc;
        while (it.hasNext()) {
            result = biFun.apply(result, it.next());
        }
        return result;
    }

    public static <T, R> T foldr(Iterable<R> from, T acc, BiFunction<? super R,? super T,? extends T> biFun) {
        Iterator<R> it = from.iterator();
        T result = acc;
        while (it.hasNext()) {
            result = biFun.apply(it.next(), result);
        }
        return result;
    }
}
