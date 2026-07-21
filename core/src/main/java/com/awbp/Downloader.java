package com.awbp;

import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadedFile;

public interface Downloader {

    DownloadedFile download(DownloadJob job);
}
