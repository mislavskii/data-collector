package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Utils {
    private static final Logger logger = LogManager.getLogger(Utils.class);
    static final File LOGDIR = new File("logs");

    private Utils() {
    }

    static void logError(Logger logger, Exception e) {
        StringBuilder message = new StringBuilder(e.getMessage());
        Arrays.stream(e.getStackTrace()).forEach(
                element -> message.append(System.lineSeparator()).append("\t".repeat(2)).append(element));
        logger.log(Level.ERROR, message);
    }

    static void cleanUpLogDir() {
        int maxFileCount = 5;
        logger.log(Level.INFO, "Cleaning the logs directory...");
        File[] logFiles = LOGDIR.listFiles();
        if (logFiles != null) {
            Stream.of(logFiles).filter(file -> file.getName().startsWith("execution"))
                    .sorted(Comparator.reverseOrder())
                    .skip(maxFileCount)
                    .forEach(file -> logger.log(
                            Level.INFO, String.format("deleted %s : %s", file.getName(), file.delete())
                    ));
        }
    }

}