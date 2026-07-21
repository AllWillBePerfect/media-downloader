package com.awbp;

/*import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadedFile;
import com.awbp.ports.DownloadJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;*/

/*@Component
@RequiredArgsConstructor
@Slf4j
public class YtDlpDownloader implements Downloader {

    private final DownloadJobRepository repository;

    @Override
    public DownloadedFile download(DownloadJob job) {

        Long userId = repository.findById(job.id())
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + job.id()))
                .userId();

        String userHome = System.getProperty(PathConstants.HOME_DIR);

        Path outputDirectory = Paths.get(
                userHome,
                PathConstants.YT_DLP_FOLDER,
                PathConstants.USERS_FOLDER,
                userId.toString(),
                job.id().toString()
        );

        try {
            Files.createDirectories(outputDirectory);

            log.info("Starting download for {}", job.url());

            Path outputTemplate = outputDirectory.resolve("%(title)s.%(ext)s");

            Path downloadedFile = resolveOutputFile(job.url(), outputTemplate);

            ProcessBuilder builder = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "b",
                    "--no-playlist",
                    "-o", outputTemplate.toString(),
                    job.url()
            );

            builder.inheritIO();

            Process process = builder.start();

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException(
                        "yt-dlp finished with exit code " + exitCode
                );
            }

            if (!Files.exists(downloadedFile)) {
                throw new RuntimeException(
                        "Downloaded file not found: " + downloadedFile
                );
            }

            long fileSize = Files.size(downloadedFile);

            String fileName = downloadedFile.getFileName().toString();

            String mimeType = Files.probeContentType(downloadedFile);

            return DownloadedFile.create(
                    UUID.randomUUID(),
                    job.id(),
                    downloadedFile.toString(),
                    fileName,
                    fileSize,
                    mimeType != null
                            ? mimeType
                            : "application/octet-stream"
            );

        } catch (IOException e) {
            throw new RuntimeException("IO error during yt-dlp execution", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Download interrupted", e);
        }
    }

    private Path resolveOutputFile(String url, Path outputTemplate)
            throws IOException, InterruptedException {

        ProcessBuilder builder = new ProcessBuilder(
                "yt-dlp",
                "--get-filename",
                "-o",
                outputTemplate.toString(),
                url
        );

        Process process = builder.start();

        String fileName;

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(process.getInputStream()))) {

            fileName = reader.readLine();
        }

        int exitCode = process.waitFor();

        if (exitCode != 0 || fileName == null || fileName.isBlank()) {
            throw new RuntimeException(
                    "Unable to determine output filename"
            );
        }

        return Paths.get(fileName.trim());
    }
}*/


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

            // Важно: каждый аргумент должен быть ОТДЕЛЬНЫМ элементом массива/списка
/*
ProcessBuilder builder = new ProcessBuilder(
                    "yt-dlp",
                    "-f", "b", // Автоматический выбор лучшего качества (best), где видео и аудио уже склеены
                    "--no-playlist", // Скачивать только видео по ссылке, игнорировать плейлисты
                    "-o", outputDirectory.resolve("%(title)s.%(ext)s").toString(), // Шаблон имени файла
                    job.url() // Ссылка на видео из вашей сущности job
            );

            // Перенаправляем вывод ошибок yt-dlp в консоль Spring Boot для удобного дебага
            builder.inheritIO();
*/


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

            // Ищем самый новый файл (только что скачанный)
//            Path downloadedFile = findNewestFile(outputDirectory);
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


/*
package com.awbp;

import com.awbp.domain.DownloadJob;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Comparator;
import java.util.stream.Stream;

@Component
public class YtDlpDownloader implements Downloader {

    @Override
    public DownloadResult download(DownloadJob job) {
        Path outputDirectory = Paths.get("downloads");
        try {
            Files.createDirectories(outputDirectory);
            ProcessBuilder builder = new ProcessBuilder(
*/
/*                    "yt-dlp",
                    "--no-playlist",
                    "-o",
//                    "-f",
                    outputDirectory.resolve("%(title)s.%(ext)s").toString(),
                    job.url()*//*

                    "echo hello"
            );
            Process process = builder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("yt-dlp finished with exit code " + exitCode);
            }
            Path downloadedFile = findNewestFile(outputDirectory);
            return new DownloadResult(
                    downloadedFile,
                    Files.size(downloadedFile)
            );
        } catch (IOException e) {
            throw new RuntimeException("IO error during download", e);
        } catch (InterruptedException e) {
            // Просто пробрасываем ошибку выше, НЕ трогая флаг прерывания потока
            throw new RuntimeException("Download process was interrupted externally", e);
        }

    }

    private Path findNewestFile(Path directory) throws IOException {
        try (Stream<Path> files = Files.list(directory)) {
            return files
                    .max(Comparator.comparing(this::lastModified))
                    .orElseThrow();
        }
    }

    private FileTime lastModified(Path file) {
        try {
            return Files.getLastModifiedTime(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
*/
