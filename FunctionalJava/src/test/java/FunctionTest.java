import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

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
        Function<Number, String> addZeroChar = new Function<Number, String>() {
            @Override
            public String apply(Number from) {
                return String.valueOf(from) + "0";
            }
        };
        Function<Integer, Long> sqrt = new Function<Integer, Long>() {
            @Override
            public Long apply(Integer from) {
                return Long.valueOf(from * from);
            }
        };
        assertEquals(new Long(81), intFromStr.compose(sqrt).apply("-9"));
        assertEquals("810", sqrt.compose(addZeroChar).apply(-9));
        assertEquals(new Integer(810), sqrt.compose(addZeroChar.compose(intFromStr)).apply(-9));
    }

}