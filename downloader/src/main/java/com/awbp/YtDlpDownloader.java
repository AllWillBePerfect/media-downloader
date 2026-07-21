package com.awbp;

import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadedFile;
import com.awbp.ports.DownloadJobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@Slf4j
public class YtDlpDownloader implements Downloader {

    private final DownloadJobRepository repository;
    private final Path storageRoot;

    public YtDlpDownloader(
            DownloadJobRepository repository,
            @Value("${app.storage.root}") String storageRoot
    ) {
        this.repository = repository;
        this.storageRoot = Path.of(storageRoot)
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public DownloadedFile download(DownloadJob job) {

        Long userId = repository.findById(job.id()).orElseThrow(
                () -> new IllegalArgumentException("User not found: " + job.userId())
        ).userId();

        Path outputDirectory = storageRoot.resolve(
                Path.of(
                        PathConstants.USERS_FOLDER,
                        userId.toString(),
                        job.id().toString()
                )
        );

        try {
            Files.createDirectories(outputDirectory);
            log.info("Starting download for URL: {} into directory: {}", job.url(), outputDirectory);

            ProcessBuilder builder = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "b",
                    "--no-playlist",
                    "--print", "after_move:filepath",
                    "-o", outputDirectory.resolve("%(title)s.%(ext)s").toString(),
                    job.url()
            );

            builder.redirectErrorStream(true);

            Process process = builder.start();

            String downloadedPath;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {

                downloadedPath = null;
                String line;

                while ((line = reader.readLine()) != null) {
                    log.info("yt-dlp: {}", line);

                    downloadedPath = line;
                }
            }



            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("yt-dlp finished with exit code " + exitCode);
            }

            Path downloadedFile = Paths.get(downloadedPath);

            long fileSize = Files.size(downloadedFile);

            String fileName = downloadedFile.getFileName().toString();

            String mimeType = Files.probeContentType(downloadedFile);

            return DownloadedFile.create(
                    UUID.randomUUID(),
                    job.id(),
                    downloadedFile.toString(),
                    fileName,
                    fileSize,
                    mimeType != null ? mimeType : "application/octet-stream"
            );

        } catch (IOException e) {
            throw new RuntimeException("IO error during yt-dlp execution", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Download process was interrupted", e);
        }
    }

    private Path findNewestFile(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files
                    .filter(Files::isRegularFile) // Игнорируем подпапки, если они есть
                    .max(Comparator.comparing(this::lastModified))
                    .orElseThrow(() -> new RuntimeException("No files found in directory: " + directory));
        }
    }

    private FileTime lastModified(Path file) {
        try {
            return Files.getLastModifiedTime(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get last modified time for file: " + file, e);
        }
    }
}

