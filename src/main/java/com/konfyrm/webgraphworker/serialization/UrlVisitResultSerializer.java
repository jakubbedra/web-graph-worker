package com.konfyrm.webgraphworker.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konfyrm.webgraphworker.domain.message.UrlVisitResult;
import org.apache.kafka.common.serialization.Serializer;

public class UrlVisitResultSerializer implements Serializer<UrlVisitResult> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(String s, UrlVisitResult urlVisitResult) {
        try {
            return objectMapper.writeValueAsBytes(urlVisitResult);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing UrlVisitResult", e);
        }
    }

}