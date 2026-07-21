package com.awbp.application;

import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadedFile;
import com.awbp.ports.DownloadJobRepository;
import com.awbp.ports.DownloadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDownloadedFileService {

    private final DownloadJobRepository downloadJobRepository;
    private final DownloadedFileRepository downloadedFileRepository;

    public DownloadedFile getFile(UUID jobId) {
       return downloadedFileRepository.getByJobId(jobId).orElseThrow();
    }
}
