package io.viettel.iva;

import io.viettel.config.SpringConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author : Giangbb
 * @since : 09/04/2020, Thu
 **/
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws FileNotFoundException, IOException {
        logger.debug("Starting application context!!!!!!!");

        @SuppressWarnings("resource")
        AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(
                SpringConfig.class);
        ctx.registerShutdownHook();

        ctx.close();

    }


}
