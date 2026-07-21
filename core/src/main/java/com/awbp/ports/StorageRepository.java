package com.awbp.ports;

import java.util.UUID;

public interface StorageRepository {

    boolean tryReserve(UUID jobId, long bytes);

    void commit(UUID jobId, long actualBytes);

    void release(UUID jobId);
}
