package com.awbp;

import java.nio.file.Path;

public record DownloadResult(
        Path file,
        long size
) {
}
