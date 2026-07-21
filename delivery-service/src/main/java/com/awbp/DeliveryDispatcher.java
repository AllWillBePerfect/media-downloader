package com.awbp;

import com.awbp.domain.DeliveryTarget;
import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadJob;
import com.awbp.ports.DeliveryTargetRepository;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DeliveryDispatcher {

    private final Map<DeliveryType, DeliveryHandler> handlers;
    private final DeliveryTargetRepository deliveryTargetRepository;

    public DeliveryDispatcher(List<DeliveryHandler> handlers, DeliveryTargetRepository deliveryTargetRepository) {

        this.handlers = handlers.stream()
                .collect(Collectors.toMap(
                        DeliveryHandler::supports,
                        Function.identity()
                ));
        this.deliveryTargetRepository = deliveryTargetRepository;
    }

    public void deliver(
            DownloadJob job,
            Path file,
            Long size
    ) {

        DeliveryTarget deliveryTarget = deliveryTargetRepository.getByJobId(job.id()).orElseThrow();

        DeliveryHandler handler =
                handlers.get(deliveryTarget.deliveryType());

        if (handler == null) {
            throw new IllegalStateException(
                    "No handler for " + deliveryTarget.deliveryType()
            );
        }

        handler.deliver(job, file, size, deliveryTarget.target());
    }

}