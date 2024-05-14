package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebCrawlerImpl implements WebCrawler {

    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerImpl.class);

    private final UrlManager urlManager;
    private final HtmlDownloader htmlDownloader;
    private final NeighbouringUrlProcessor neighbouringUrlProcessor;

    @Autowired
    public WebCrawlerImpl(
            @Qualifier("jsoupUrlManager") UrlManager urlManager,
            @Qualifier("jsoupHtmlDownloader") HtmlDownloader htmlDownloader,
            @Qualifier("neighbouringUrlProcessorImpl") NeighbouringUrlProcessor neighbouringUrlProcessor
    ) {
        this.urlManager = urlManager;
        this.htmlDownloader = htmlDownloader;
        this.neighbouringUrlProcessor = neighbouringUrlProcessor;
    }

    @Override
    public Map<String, Set<String>> crawl(String startUrl, int maxVisitedNodes) {
        Set<String> disallowedPatterns = urlManager.getDisallowedPatterns(startUrl);

        if (!isCrawlAllowed(startUrl, disallowedPatterns)) {
            LOGGER.warn("Crawling not allowed for start url: " + startUrl);
            return Collections.emptyMap();
        }

        Map<String, Set<String>> visitedUrls = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startUrl);

        int visitedNodes = 0;
        while (!queue.isEmpty() && visitedNodes < maxVisitedNodes) {
            String currentUrl = queue.poll();
            visitedNodes++;

//            if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Crawling: " + currentUrl);
//            }

            htmlDownloader.downloadHtmlDocument(currentUrl);
            Set<String> neighbouringUrls = urlManager.getNeighbouringUrls(currentUrl);
            Set<String> processedNeighbouringUrls = neighbouringUrlProcessor.processNeighbouringUrls(currentUrl, neighbouringUrls);

            visitedUrls.put(currentUrl, processedNeighbouringUrls);

            if (visitedNodes < maxVisitedNodes) {
                processedNeighbouringUrls.stream()
                        .filter(url -> !visitedUrls.containsKey(url))
                        .filter(url -> isCrawlAllowed(url, disallowedPatterns))
                        .forEach(queue::add);
            }
        }
        return visitedUrls;
    }

    @Override
    public Set<String> crawl(String url) {
        Set<String> disallowedPatterns = urlManager.getDisallowedPatterns(url);

        if (!isCrawlAllowed(url, disallowedPatterns)) {
            LOGGER.warn("Crawling not allowed for start url: " + url);
            return Collections.emptySet();
        }

//            if (LOGGER.isDebugEnabled()) {
        LOGGER.info("Crawling: " + url);
//            }

        htmlDownloader.downloadHtmlDocument(url);
        Set<String> neighbouringUrls = urlManager.getNeighbouringUrls(url);
        Set<String> processedNeighbouringUrls = neighbouringUrlProcessor.processNeighbouringUrls(url, neighbouringUrls);

        return processedNeighbouringUrls.stream()
                .filter(u -> isCrawlAllowed(u, disallowedPatterns))
                .collect(Collectors.toSet());
    }

    private boolean isCrawlAllowed(String url, Set<String> disallowedPatterns) {
        for (String pattern : disallowedPatterns) {
            if (UrlUtils.containsPattern(url, pattern)) {
                return false;
            }
        }
        return true;
    }

}