package com.awbp.rest.dto;

import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadType;

public record CreateDownloadRequest(
        DeliveryType deliveryType,
        Long userId,
        String url,
        DownloadType type,
        String target
) {
}
