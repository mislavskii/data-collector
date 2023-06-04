package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void cleanUpLogDir() throws IOException, InterruptedException {
        File logDir = new File("logs/");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        while (countExecutionLogFiles(logDir) < 6) {
            File filler = new File("logs/execution_" + formatter.format(LocalDateTime.now()) + ".log");
            System.out.printf("dummy log file created: %s - %s%n", filler.getName(), filler.createNewFile());
            TimeUnit.MILLISECONDS.sleep(1100);
        }
        Utils.cleanUpLogDir();
        assertTrue(countExecutionLogFiles(logDir) <= 5);
    }

    private long countExecutionLogFiles(File logDir) {
        return Stream.of(Objects.requireNonNull(logDir.listFiles()))
                .filter(file -> file.getName().startsWith("execution")).count();
    }
}