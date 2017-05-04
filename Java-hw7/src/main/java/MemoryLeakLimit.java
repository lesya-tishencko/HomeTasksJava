import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Created by lesya on 04.05.2017.
 */
public class MemoryLeakLimit implements TestRule {
    private long memoryLimit = 0;

    public void limit(long mb) {
        memoryLimit = mb * 1024 * 1024;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Runtime runtime = Runtime.getRuntime();
                runtime.gc();
                long freeMemBefore = runtime.freeMemory();

                base.evaluate();

                runtime.gc();
                long freeMemAfter = runtime.freeMemory();

                if (freeMemAfter - freeMemBefore > memoryLimit)
                    throw new Exception("Memory leak limit is exceeded");
            }
        };
    }
}
