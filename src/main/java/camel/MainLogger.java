package camel;

import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by pizmak on 2016-04-25.
 */
public class MainLogger {
    private static final Logger logger = LoggerFactory.getLogger("camelLogger");

    @Handler
    public void logMessage(String message){
        System.out.println("---------------------------------------------------------MainLogger"+message);
        logger.info("Camel info"+message);
    }
}
