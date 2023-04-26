package org.example;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileFinder {
    private final String[] EXTENSIONS = {"json", "csv"};
    Map<String, List<File>> results;

    public FileFinder() {
        this.results = new HashMap<>();
        for (String extension : EXTENSIONS) {
            results.put(extension, new ArrayList<>());
        }
    }

    Map<String, List<File>> findDataFiles(String path) {
        try (Stream<Path> stream = Files.walk(Path.of(path))) {
            stream.forEach(p -> {
                File file = new File(p.toString());
                if (file.isFile()) {
                    checkFile(file);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    Map<String, List<File>> findDataFilesB(String path) {
        try {
            File[] files = new File(path).listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    checkFile(file);
                } else {
                    findDataFilesB(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
