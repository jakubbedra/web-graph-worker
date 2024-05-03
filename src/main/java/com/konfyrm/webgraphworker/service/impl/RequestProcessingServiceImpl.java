package com.konfyrm.webgraphworker.service.impl;

import com.konfyrm.webgraphworker.crawler.WebCrawler;
import com.konfyrm.webgraphworker.service.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

}