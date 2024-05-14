package com.konfyrm.webgraphworker.listener;

import com.google.common.collect.Iterables;
import com.konfyrm.webgraphworker.domain.message.UrlNode;
import com.konfyrm.webgraphworker.domain.message.UrlVisitRequest;
import com.konfyrm.webgraphworker.domain.message.UrlVisitResult;
import com.konfyrm.webgraphworker.service.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.konfyrm.webgraphworker.domain.KafkaTopicConstants.*;

@Component
public class UrlVisitRequestListener {

    public static final int REQUEST_PARTITION = 10;

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

    @KafkaListener(topics = REQUEST_TOPIC, groupId = DEFAULT_GROUP, containerFactory = "urlVisitRequestListenerContainerFactory", batch = "true")
    public void handleUrlVisitRequest(List<UrlVisitRequest> requests) {
        Map<String, List<UrlVisitRequest>> requestsByExecution = requests.stream()
                .collect(Collectors.groupingBy(UrlVisitRequest::getExecutionUuid));
        for (String executionUuid : requestsByExecution.keySet()) {
            List<String> urls = requestsByExecution.get(executionUuid).stream()
                    .map(UrlVisitRequest::getUrl)
                    .toList();
            List<UrlNode> results = requestProcessingService.processRequests(urls);
            Iterable<List<UrlNode>> partition = Iterables.partition(results, REQUEST_PARTITION); // todo: change to use calculator. estimate avg object size
            for (List<UrlNode> urlNodes : partition) {
                UrlVisitResult result = UrlVisitResult.builder()
                        .executionUuid(executionUuid)
                        .nodes(urlNodes)
                        .build();
                kafkaTemplate.send(RESULT_TOPIC, result);
            }
        }
    }

}
