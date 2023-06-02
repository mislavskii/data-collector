package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

public class Utils {

    private Utils() {
    }

    static void logError(Logger logger, Exception e) {
        StringBuilder message = new StringBuilder(e.getMessage());
        Arrays.stream(e.getStackTrace()).forEach(
                element -> message.append(System.lineSeparator()).append("\t".repeat(2)).append(element));
        logger.log(Level.ERROR, message);
    }

    static void cleanUpLogDir() {
        String logsDir = "logs";
        try (Stream<Path> logFiles = Files.walk(Path.of(logsDir), 1)) {
            logFiles.filter(Files::isRegularFile).sorted(Comparator.reverseOrder()).skip(3).forEach(file -> {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}