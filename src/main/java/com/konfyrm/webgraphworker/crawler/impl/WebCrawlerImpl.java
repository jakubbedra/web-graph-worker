package com.konfyrm.webgraphworker.crawler.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.konfyrm.webgraphworker.crawler.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class WebCrawlerImpl implements WebCrawler {

    private static final Cache<String, Set<String>> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

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
    public Set<String> crawl(String executionUuid, String url, boolean downloadFiles) {
        Set<String> disallowedPatterns = fetchDisallowedPatterns(executionUuid);

        if (!isCrawlAllowed(url, disallowedPatterns)) {
            LOGGER.warn("Crawling not allowed for start url: " + url);
            return Collections.emptySet();
        }

        LOGGER.info("Crawling: " + url);

        if (downloadFiles) {
            htmlDownloader.downloadHtmlDocument(url);
        }
        Set<String> neighbouringUrls = urlManager.getNeighbouringUrls(url);
        Set<String> processedNeighbouringUrls = neighbouringUrlProcessor.processNeighbouringUrls(url, neighbouringUrls);

        return processedNeighbouringUrls.stream()
                .filter(u -> isCrawlAllowed(u, disallowedPatterns))
                .collect(Collectors.toSet());
    }

    private Set<String> fetchDisallowedPatterns(String executionUuid) {
        try {
            return cache.get(executionUuid, () -> urlManager.getDisallowedPatterns(executionUuid));
        } catch (ExecutionException e) {
            LOGGER.warn("Error fetching value from cache: " + e.getMessage());
        }
        return Collections.emptySet();
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
