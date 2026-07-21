package com.awbp.ports;

import com.awbp.contact.DeliveryRequestedEvent;

public interface DeliveryRequestedPublisher {
    void publish(DeliveryRequestedEvent event);

}
