package com.awbp.contact;

import java.util.UUID;

public record DeliveryRequestedEvent(
        UUID jobId,
        String filePath,
        Long size
) {}
