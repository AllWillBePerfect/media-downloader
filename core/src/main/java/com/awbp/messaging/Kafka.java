package com.awbp.messaging;

public final class Kafka {

    private Kafka() {}

    public static final class Topics {

        public static final String DOWNLOAD_REQUESTS = "download-requests";
        public static final String DELIVERY_REQUESTS = "delivery-requests";
        public static final String DOWNLOAD_STATUS_CHANGES =
                "download-status-changes";
        private Topics() {}
    }

    public static final class ConsumerGroups {

        public static final String DOWNLOAD_WORKERS = "download-workers";
        public static final String DELIVERY_WORKERS = "delivery-workers";
        public static final String STATUS_NOTIFIERS =
                "status-notifiers";

        private ConsumerGroups() {}
    }
}