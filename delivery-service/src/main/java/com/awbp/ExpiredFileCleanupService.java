package com.awbp;

import com.awbp.domain.DownloadedFile;
import com.awbp.ports.DownloadedFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExpiredFileCleanupService {

    private final DownloadedFileRepository downloadedFileRepository;

    @Scheduled(initialDelay = 0, fixedDelay = 5 * 60 * 1000)
    public void cleanup() {
        for (DownloadedFile file :
                downloadedFileRepository.findExpired(Instant.now())) {
            try {
                Files.deleteIfExists(Path.of(file.path()));

                downloadedFileRepository.deleteAndReleaseStorage(file.id());

                log.info("Deleted expired file {}", file.id());
            } catch (IOException exception) {
                log.warn(
                        "Failed to delete expired file {}",
                        file.path(),
                        exception
                );
            }
        }
    }

    public void deleteNow(DownloadedFile file) {
        deleteFile(file);
    }

    private void deleteFile(DownloadedFile file) {
        try {
            Files.deleteIfExists(Path.of(file.path()));

            downloadedFileRepository.deleteAndReleaseStorage(file.id());

            log.info("Deleted file {}", file.id());
        } catch (IOException exception) {
            log.warn("Failed to delete file {}", file.path(), exception);
        }
    }
}
