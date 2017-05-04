import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lesya on 06.04.2017.
 */
public class BiFunctionTest {
    @Test
    public void compose() throws Exception {
        BiFunction<Integer, Integer, Long> pow = new BiFunction<Integer,Integer,Long>() {
            @Override
            public Long apply(Integer arg1, Integer arg2) {
                return Math.round(Math.pow(arg1, arg2));
            }
        };
        Function <Number, String> numToStr = new Function<Number, String>() {
            @Override
            public String apply(Number from) {
                return String.valueOf(from);
            }
        };
        assertEquals("10000000000", pow.compose(numToStr).apply(100000, 2));
        assertEquals("10000000000", pow.compose(numToStr).apply(10, 10));
    }

    @Test
    public void bind1() throws Exception {
        BiFunction<Integer, Integer, Double> pow = new BiFunction<Integer, Integer, Double>() {
            @Override
            public Double apply(Integer arg1, Integer arg2) {
                return Math.pow(arg1, arg2);
            }
        };
        Function<Integer, Double> pow10 = pow.bind1(10);
        assertEquals(new Double(100), pow10.apply(2));
        assertEquals(new Double(1000000), pow10.apply(6));
    }

    @Test
    public void bind2() throws Exception {
        BiFunction<Integer, Integer, Double> pow = new BiFunction<Integer, Integer, Double>() {
            @Override
            public Double apply(Integer arg1, Integer arg2) {
                return Math.pow(arg1, arg2);
            }
        };
        Function<Integer, Double> sqrt = pow.bind2(2);
        assertEquals(new Double(25), sqrt.apply(5));
        assertEquals(new Double(10000), sqrt.apply(100));
    }

    @Test
    public void carry() throws Exception {
        BiFunction<Integer, Integer, String> sumToStr = new BiFunction<Integer, Integer, String>() {
            @Override
            public String  apply(Integer arg1, Integer arg2) {
                return String.valueOf(arg1 + arg2);
            }
        };
        Function<Integer, String> succToStr = sumToStr.carry().apply(1);
        assertEquals("101", succToStr.apply(100));
        assertEquals("0", succToStr.apply(-1));
    }

}