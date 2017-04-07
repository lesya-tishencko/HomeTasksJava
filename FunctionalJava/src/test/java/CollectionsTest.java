import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by lesya on 06.04.2017.
 */
public class CollectionsTest {
    @Test
    public void map() throws Exception {
        Function<Integer, String> intToStr = new Function<Integer, String>() {
            @Override
            public String apply(Integer from) {
                return from.toString();
            }
        };
        assertEquals(Arrays.asList("1", "-2", "3", "-4", "5"), Collections.map(Arrays.asList(1, -2, 3, -4, 5), intToStr));
    }

    @Test
    public void filter() throws Exception {
        Predicate<Character> isDigit = new Predicate<Character>() {
            @Override
            public Boolean apply(Character from) {
                return Character.isDigit(from);
            }
        };
        assertEquals(Arrays.asList('1', '2', '3'), Collections.filter(Arrays.asList('1', 'a', '2', 'b', '3'), isDigit));
    }

    @Test
    public void takeWhile() throws Exception {
        Predicate<Object> notNull = new Predicate<Object>() {
            @Override
            public Boolean apply(Object from) {
                return from != null;
            }
        };
        assertEquals(Arrays.asList('1', "hello", 1), Collections.takeWhile(Arrays.asList('1', "hello", 1, null, 2), notNull));
    }

    @Test
    public void takeUnless() throws Exception {
        Predicate<Object> isNull = new Predicate<Object>() {
            @Override
            public Boolean apply(Object from) {
                return from == null;
            }
        };
        assertEquals(Arrays.asList('1', "hello", 1), Collections.takeUnless(Arrays.asList('1', "hello", 1, null, 2), isNull));
    }

    @Test
    public void foldl() throws Exception {
        BiFunction<String, String, String> concat = new BiFunction<String, String, String>() {
            @Override
            public String apply(String arg1, String arg2) {
                return arg1.concat(arg2);
            }
        };
        assertEquals("Hello, perfect and sunny day", Collections.foldl(Arrays.asList("perfect", " and", " sunny", " day"), "Hello, ", concat));
    }

    @Test
    public void foldr() throws Exception {
        BiFunction<String, String, String> concat = new BiFunction<String, String, String>() {
            @Override
            public String apply(String arg1, String arg2) {
                return arg1.concat(arg2);
            }
        };
        assertEquals("day sunny and perfect, hello", Collections.foldr(Arrays.asList("perfect", "and ", "sunny ", "day "), ", hello", concat));
    }

}