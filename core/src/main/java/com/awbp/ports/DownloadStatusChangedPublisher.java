package com.awbp.ports;

import com.awbp.contact.DownloadStatusChangedEvent;

public interface DownloadStatusChangedPublisher {

    void publish(DownloadStatusChangedEvent event);
}