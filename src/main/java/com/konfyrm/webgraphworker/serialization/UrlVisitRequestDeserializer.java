package com.konfyrm.webgraphworker.serialization;

import com.konfyrm.webgraphworker.domain.message.UrlVisitRequest;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UrlVisitRequestDeserializer implements Deserializer<UrlVisitRequest> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UrlVisitRequest deserialize(String s, byte[] data) {
        try {
            return objectMapper.readValue(data, UrlVisitRequest.class);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing UrlVisitRequest: " + e.getMessage(), e);
        }
    }

}