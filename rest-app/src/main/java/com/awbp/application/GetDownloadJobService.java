package com.awbp.application;

import com.awbp.rest.dto.DownloadResponse;
import com.awbp.domain.DownloadJob;
import com.awbp.exception.DownloadJobNotFoundException;
import com.awbp.mapper.DownloadMapper;
import com.awbp.ports.DownloadJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDownloadJobService {

    private final DownloadJobRepository repository;

    @Transactional(readOnly = true)
    public DownloadResponse get(UUID id) {

        DownloadJob job = repository.findById(id)
                .orElseThrow(() -> new DownloadJobNotFoundException(id));

        return DownloadMapper.toResponse(job);
    }
}