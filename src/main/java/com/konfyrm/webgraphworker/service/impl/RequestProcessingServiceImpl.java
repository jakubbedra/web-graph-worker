package com.konfyrm.webgraphworker.service.impl;

import com.konfyrm.webgraphworker.crawler.WebCrawler;
import com.konfyrm.webgraphworker.domain.message.UrlNode;
import com.konfyrm.webgraphworker.domain.message.UrlVisitResult;
import com.konfyrm.webgraphworker.service.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RequestProcessingServiceImpl implements RequestProcessingService {

    private final WebCrawler webCrawler;

    @Autowired
    public RequestProcessingServiceImpl(
            @Qualifier("webCrawlerImpl") WebCrawler webCrawler
    ) {
        this.webCrawler = webCrawler;
    }

    @Override
    public Map<String, Set<String>> processRequest(String startUrl, int maxVisitedNodes) {
        return webCrawler.crawl(startUrl, maxVisitedNodes);
    }

    @Override
    public List<UrlNode> processRequests(String executionUuid, List<String> urls) {
        List<UrlNode> results = new LinkedList<>();
        for (String url : urls) {
            Set<String> neighbours = webCrawler.crawl(executionUuid, url);
            UrlNode urlNode = UrlNode.builder()
                    .url(url)
                    .neighbours(neighbours)
                    .build();
            results.add(urlNode);
        }
        return results;// todo
    }

}
