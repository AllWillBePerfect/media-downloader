package com.awbp;

import com.awbp.domain.DownloadJob;

public interface VideoMetadataProvider {

    long getExpectedSize(DownloadJob job);
}
