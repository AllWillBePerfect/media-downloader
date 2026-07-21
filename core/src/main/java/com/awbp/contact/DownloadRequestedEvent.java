package com.awbp.contact;

import java.util.UUID;

public record DownloadRequestedEvent(
        UUID jobId
) {
}
