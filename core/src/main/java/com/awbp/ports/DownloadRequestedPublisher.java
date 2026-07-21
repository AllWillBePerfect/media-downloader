package com.awbp.ports;

import com.awbp.contact.DownloadRequestedEvent;

public interface DownloadRequestedPublisher {
    void publish(DownloadRequestedEvent event);
}
