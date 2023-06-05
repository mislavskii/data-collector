package org.example;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {
    private final File LOGDIR = new File("logs");
    private final String FILE_NAME_ROOT = "execution_";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private long countExecutionLogFiles() {
        return Stream.of(Objects.requireNonNull(LOGDIR.listFiles()))
                .filter(file -> file.getName().startsWith(FILE_NAME_ROOT)).count();
    }

    private long getEarliestExecutionLogFileTime() {
        return Stream.of(Objects.requireNonNull(LOGDIR.listFiles()))
                .filter(file -> file.getName().startsWith(FILE_NAME_ROOT))
                .sorted().findFirst().map(File::lastModified).orElse(0L);
    }

    private void fillLogDir() throws IOException {
        int backwardTimeShift = 180;
        long earliestTimeMillis = getEarliestExecutionLogFileTime();
        long earliestTimeSec = (earliestTimeMillis == 0 ? System.currentTimeMillis() : earliestTimeMillis) / 1000;
        while (countExecutionLogFiles() < 7) {
            earliestTimeSec -= backwardTimeShift;
            String fileNameSuffix = formatter.format(
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(earliestTimeSec), ZoneId.systemDefault())
            );
            File filler = new File( LOGDIR + "/"
                    + FILE_NAME_ROOT
                    +  fileNameSuffix
                    + ".log");
            System.out.printf("dummy log file created: %s - %s%n", filler.getName(), filler.createNewFile());
        }
    }

    @Test
    void cleanUpLogDir() throws IOException {
    fillLogDir();
    Utils.cleanUpLogDir();
    assertTrue(countExecutionLogFiles() <= 5);
    }

}