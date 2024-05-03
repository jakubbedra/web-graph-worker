package com.konfyrm.webgraphworker.listener;

import com.konfyrm.webgraphworker.domain.message.UrlVisitRequest;
import com.konfyrm.webgraphworker.domain.message.UrlVisitResult;
import com.konfyrm.webgraphworker.service.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.konfyrm.webgraphworker.domain.KafkaTopicConstants.*;

@Component
public class UrlVisitRequestListener {

    private final RequestProcessingService requestProcessingService;
    private final KafkaTemplate<String, UrlVisitResult> kafkaTemplate;

    @Autowired
    public UrlVisitRequestListener(
            @Qualifier("requestProcessingServiceImpl") RequestProcessingService requestProcessingService,
            KafkaTemplate<String, UrlVisitResult> kafkaTemplate
    ) {
        this.requestProcessingService = requestProcessingService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = REQUEST_TOPIC, groupId = DEFAULT_GROUP, containerFactory = "urlVisitRequestListenerContainerFactory")
    public void handleUrlVisitRequest(UrlVisitRequest request) {
        Map<String, Set<String>> nodes = requestProcessingService.processRequest(request.getUrl(), request.getMaxVisitedNodes());
        UrlVisitResult result = UrlVisitResult.builder()
                .startUrl(request.getUrl())
                .neighbours(nodes)
                .build();
        kafkaTemplate.send(RESULT_TOPIC, result);
    }

}