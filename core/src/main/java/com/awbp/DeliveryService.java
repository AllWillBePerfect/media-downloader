package com.awbp;

import com.awbp.domain.DownloadJob;

public interface DeliveryService {

    void deliver(DownloadJob job, DownloadResult result);

}