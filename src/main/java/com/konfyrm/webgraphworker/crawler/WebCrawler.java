package com.konfyrm.webgraphworker.crawler;

import java.util.Set;

public interface WebCrawler {
    Set<String> crawl(String executionUuid, String startUrl, boolean downloadFiles);
}