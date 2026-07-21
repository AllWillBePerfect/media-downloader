package com.awbp;

import com.awbp.domain.DownloadJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@Slf4j
public class YtDlpVideoMetadataProvider implements VideoMetadataProvider {

    @Override
    public long getExpectedSize(DownloadJob job) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "yt-dlp",
                "--no-playlist",
                "--skip-download",
                "-f", "b",
                "--print", "%(filesize,filesize_approx)s",
                job.url()
        );

        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

        try {
            Process process = processBuilder.start();

            String size;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                size = reader.readLine();
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "yt-dlp не смог получить метаданные: код завершения " + exitCode
                );
            }

            if (size == null || size.isBlank()) {
                throw new IllegalStateException(
                        "yt-dlp не вернул размер файла для " + job.url()
                );
            }

            long expectedSize = Long.parseLong(size.trim());

            if (expectedSize <= 0) {
                throw new IllegalStateException(
                        "yt-dlp вернул некорректный размер: " + expectedSize
                );
            }

            log.info("Expected size for job {}: {} bytes", job.id(), expectedSize);
            return expectedSize;
        } catch (IOException exception) {
            throw new IllegalStateException("Не удалось запустить yt-dlp", exception);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Получение метаданных было прервано", exception);
        } catch (NumberFormatException exception) {
            throw new IllegalStateException("yt-dlp вернул размер в неизвестном формате", exception);
        }
    }
}
