package com.awbp;

import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadJob;

import java.nio.file.Path;

public interface DeliveryHandler {

    DeliveryType supports();

    void deliver(
            DownloadJob job,
            Path file,
            Long size,
            String target
    );

}
