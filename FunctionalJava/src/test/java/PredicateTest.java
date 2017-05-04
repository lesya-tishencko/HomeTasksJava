import org.junit.Test;

import java.util.ArrayList;
import java.util.function.*;

import static org.junit.Assert.*;

/**
 * Created by lesya on 06.04.2017.
 */
public class PredicateTest {
    @Test
    public void not() throws Exception {
        Predicate<String> startWithUpper = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                return Character.isUpperCase(from.toCharArray()[0]);
            }
        };
        assertTrue(startWithUpper.not().apply("hello"));
        assertFalse(startWithUpper.not().apply("Lesya Tishencko"));
    }

    @Test
    public void and() throws Exception {
        Predicate<String> allIsAlpha = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                boolean result = !from.isEmpty();
                for (char c: from.toCharArray()) {
                    result = result && Character.isLetter(c);
                }
                return result;
            }
        };
        Predicate<String> lazy = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                assert(0 != 0);
                return true;
            }
        };
        assertFalse(allIsAlpha.and(Predicate.ALWAYS_TRUE).apply("Hello, world!"));
        assertFalse(allIsAlpha.and(Predicate.ALWAYS_FALSE).apply("Hello, world!"));
        assertFalse(allIsAlpha.and(lazy).apply("Lazy pridacate checking"));
    }

    @Test
    public void or() throws Exception {
        Predicate<String> allIsAlpha = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                boolean result = !from.isEmpty();
                for (char c: from.toCharArray()) {
                    result = result && Character.isLetter(c);
                }
                return result;
            }
        };
        Predicate<String> allIsDigit = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                boolean result = !from.isEmpty();
                for (char c: from.toCharArray()) {
                    result = result && Character.isDigit(c);
                }
                return result;
            }
        };
        Predicate<String> lazy = new Predicate<String>() {
            @Override
            public Boolean apply(String from) {
                assert(0 != 0);
                return true;
            }
        };
        Predicate<Object> notNull = new Predicate<Object>() {
            @Override
            public Boolean apply(Object obj) {
                return obj != null;
            }
        };
        assertTrue(allIsAlpha.or(allIsDigit).apply("12345"));
        assertTrue(allIsAlpha.or(allIsDigit).apply("LesyaTishencko"));
        assertFalse(allIsAlpha.or(allIsDigit).apply("RP Pi 2B"));
        assertTrue(allIsAlpha.or(lazy).apply("LazyPredicateChecking"));
        assertTrue(allIsAlpha.or(notNull).apply("091195"));
    }

}