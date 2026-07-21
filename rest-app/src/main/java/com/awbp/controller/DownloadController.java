package com.awbp.controller;

import com.awbp.application.CreateDownloadJobService;
import com.awbp.application.GetDownloadJobService;
import com.awbp.application.GetDownloadedFileService;
import com.awbp.domain.DownloadedFile;
import com.awbp.rest.dto.CreateDownloadRequest;
import com.awbp.rest.dto.CreateDownloadResponse;
import com.awbp.rest.dto.DownloadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DownloadController {

    private final GetDownloadJobService getDownloadJobService;
    private final CreateDownloadJobService createDownloadJobService;
    private final GetDownloadedFileService getDownloadedFileService;

    @GetMapping("/downloads/{jobId}")
    public ResponseEntity<DownloadResponse> getDownloadResponse(@PathVariable UUID jobId) {
        return ResponseEntity.ok(getDownloadJobService.get(jobId));
    }

    @GetMapping("/downloads/{jobId}/file")
    public ResponseEntity<Resource> getFile(@PathVariable UUID jobId) {
        DownloadedFile downloadedFile = getDownloadedFileService.getFile(jobId);

        Resource resource = new FileSystemResource(Path.of(downloadedFile.path()));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(downloadedFile.mimeType()))
                .contentLength(downloadedFile.size())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(downloadedFile.fileName())
                                .build().toString()
                )
                .body(resource);
    }

    @PostMapping("/downloads")
    public ResponseEntity<CreateDownloadResponse> create(
            @RequestBody CreateDownloadRequest request
    ) {
        return ResponseEntity.ok(new CreateDownloadResponse(
                createDownloadJobService.create(request)
        ));
    }
}
