/**
 * Created by lesya on 23.03.2017.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.error("error message");
        LOG.info("info message");
        LOG.trace("trace message");
        LOG.warn("warn message");
        LOG.debug("debug message");
        LOG.fatal("fatal message");
    }
}
