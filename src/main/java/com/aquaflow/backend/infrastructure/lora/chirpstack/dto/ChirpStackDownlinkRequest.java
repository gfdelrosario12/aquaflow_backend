package com.aquaflow.backend.infrastructure.lora.chirpstack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChirpStackDownlinkRequest {

    private QueueItem queueItem;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueueItem {
        private String devEui;
        private boolean confirmed;
        private int fPort;
        private String data; // Base64 encoded payload

        public String getDevEui() { return devEui; }
        public void setDevEui(String devEui) { this.devEui = devEui; }
        public boolean isConfirmed() { return confirmed; }
        public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
        public int getFPort() { return fPort; }
        public void setFPort(int fPort) { this.fPort = fPort; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
    }

    public QueueItem getQueueItem() { return queueItem; }
    public void setQueueItem(QueueItem queueItem) { this.queueItem = queueItem; }
}

