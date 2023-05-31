package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileFinder {
    private Map<String, List<File>> results;
    private final String[] EXTENSIONS = {"json", "csv"};
    private final Logger logger = LogManager.getLogger(FileFinder.class);


    Map<String, List<File>> findDataFiles(String path) {
        this.results = new HashMap<>();
        for (String extension : EXTENSIONS) {
            results.put(extension, new ArrayList<>());
        }
        try (Stream<Path> stream = Files.walk(Path.of(path))) {
            stream.forEach(p -> {
                File file = new File(p.toString());
                if (file.isFile()) {
                    checkFile(file);
                }
            });
        } catch (Exception e) {
            System.out.println("the folder is non-existent or inaccessible.");
            Utils.logError(logger, e);
        }
        return results;
    }

    private void checkFile(File file) {
        results.keySet().forEach(key -> {
            if (file.getName().endsWith(key)) {
                results.get(key).add(file.getAbsoluteFile());
            }
        });
    }

}
