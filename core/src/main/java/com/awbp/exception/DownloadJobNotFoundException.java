package com.awbp.exception;

import java.util.UUID;

public class DownloadJobNotFoundException extends RuntimeException {

    public DownloadJobNotFoundException(UUID id) {
        super("Download job '%s' not found".formatted(id));
    }

}