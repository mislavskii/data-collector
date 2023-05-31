package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Utils {

    private Utils() {}

    static void logError(Logger logger, Exception e) {
        StringBuilder message = new StringBuilder(e.getMessage());
        Arrays.stream(e.getStackTrace()).forEach(
                element -> message.append(System.lineSeparator()).append("\t".repeat(2)).append(element));
        logger.log(Level.ERROR, message);
    }
}
