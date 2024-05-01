package com.konfyrm.webgraphworker.serialization;

import org.apache.kafka.common.serialization.Serializer;

public class UrlVisitRequestSerializer implements Serializer<UrlVisitRequest> {

    @Override
    public byte[] serialize(String s, UrlVisitRequest urlVisitRequest) {
        if (urlVisitRequest == null) {
            return null;
        }
        try {
            // Convert UrlProcessingRequestMessage to byte array (e.g., using JSON serialization)
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(urlVisitRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing UrlProcessingRequestMessage", e);
        }
    }

}
