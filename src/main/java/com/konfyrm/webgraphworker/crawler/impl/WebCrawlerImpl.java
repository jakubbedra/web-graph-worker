package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.HtmlDownloader;
import com.konfyrm.webgraphworker.crawler.UrlManager;
import com.konfyrm.webgraphworker.crawler.UrlUtils;
import com.konfyrm.webgraphworker.crawler.WebCrawler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WebCrawlerImpl implements WebCrawler {

    private static final Logger LOGGER = LogManager.getLogger(WebCrawlerImpl.class);

    private final UrlManager urlManager;
    private final HtmlDownloader htmlDownloader;

    @Autowired
    public WebCrawlerImpl(
            @Qualifier("jsoupUrlManager") UrlManager urlManager,
            @Qualifier("jsoupHtmlDownloader") HtmlDownloader htmlDownloader
    ) {
        this.urlManager = urlManager;
        this.htmlDownloader = htmlDownloader;
    }

    public Map<String, List<String>> crawl(String startUrl, int maxVisitedNodes) {
        Set<String> disallowedPatterns = urlManager.getDisallowedPatterns(startUrl);

        if (!isCrawlAllowed(startUrl, disallowedPatterns)) {
            LOGGER.warn("Crawling not allowed for start url: " + startUrl);
            return Collections.emptyMap();
        }

        Map<String, List<String>> visitedUrls = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startUrl);

        int visitedNodes = 0;
        while (!queue.isEmpty() && visitedNodes < maxVisitedNodes) {
            String currentUrl = queue.poll();
            visitedNodes++;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Crawling: " + currentUrl);
            }

            htmlDownloader.downloadHtmlDocument(currentUrl);
            List<String> neighbouringUrls = urlManager.getNeighbouringUrls(currentUrl);
            visitedUrls.put(currentUrl, neighbouringUrls);

            if (visitedNodes < maxVisitedNodes) {
                neighbouringUrls.stream()
                        .map(UrlUtils::trim)
                        .filter(url -> url.contains(UrlUtils.extractHost(currentUrl)))
                        .filter(url -> !visitedUrls.containsKey(url))
                        .filter(url -> isCrawlAllowed(url, disallowedPatterns))
                        .forEach(queue::add);
            }
        }
        return visitedUrls;
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