package tn.pfe.CnotConnectV1.services;

import org.springframework.stereotype.Service;

import tn.pfe.CnotConnectV1.dto.FileDetails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    // Base path of your pCloud Drive (adjust the drive letter as needed)
    private static final String PCLOUD_BASE_PATH = "P:/";  // Replace with the actual drive letter for pCloud

    /**
     * Save a file to pCloud Drive
     */
    public String saveFileToPCloud(String fileName, byte[] content) {
        try {
            Path filePath = Paths.get(PCLOUD_BASE_PATH + fileName);
            Files.write(filePath, content);
            return "File saved successfully to pCloud Drive!";
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file to pCloud Drive", e);
        }
    }

    /**
     * Read a file from pCloud Drive
     */
    public byte[] readFileFromPCloud(String fileName) {
        try {
            Path filePath = Paths.get(PCLOUD_BASE_PATH + fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file from pCloud Drive", e);
        }
    }

    /**
     * List all files in a directory on pCloud Drive
     */
    public List<FileDetails> listFilesInPCloud() {
        File directory = new File(PCLOUD_BASE_PATH);
        File[] files = directory.listFiles();

        if (files != null) {
            return Arrays.stream(files)
                .filter(File::isFile) // Only consider files, not directories
                .map(file -> new FileDetails(
                    file.getName(),
                    file.length(),
                    Instant.ofEpochMilli(file.lastModified())
                ))
                .collect(Collectors.toList());
        } else {
            return List.of(); // Return an empty list if directory is null
        }
    }
}
