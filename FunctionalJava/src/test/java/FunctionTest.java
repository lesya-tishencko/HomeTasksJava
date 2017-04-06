import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lesya on 06.04.2017.
 */
public class FunctionTest {
    @Test
    public void testApply() throws Exception {
        Function<String, Integer> intFromStr = new Function<String, Integer>() {
            @Override
            public Integer apply(String number) {
                return Integer.valueOf(number);
            }
        };
        assertEquals(new Integer(100), intFromStr.apply("100"));
        assertEquals(new Integer(-100), intFromStr.apply("-100"));
    }

    @Test
    public void compose() throws Exception {
        Function<String, Integer> intFromStr = new Function<String, Integer>() {
            @Override
            public Integer apply(String number) {
                return Integer.valueOf(number);
            }
        };
        Function<Double, String> addZeroChar = new Function<Double, String>() {
            @Override
            public String apply(Double from) {
                return String.valueOf((int)Math.ceil(from)) + "0";
            }
        };
        Function<Integer, Double> sqrt = new Function<Integer, Double>() {
            @Override
            public Double apply(Integer from) {
                return new Double(from * from);
            }
        };
        assertEquals(new Double(81), intFromStr.compose(sqrt).apply("-9"));
        assertEquals("810", sqrt.compose(addZeroChar).apply(-9));
        assertEquals(new Integer(810), sqrt.compose(addZeroChar.compose(intFromStr)).apply(-9));
    }

}