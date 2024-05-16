package com.konfyrm.webgraphworker.crawler;

import java.util.Map;
import java.util.Set;

public interface WebCrawler {
    @Deprecated
    Map<String, Set<String>> crawl(String startUrl, int maxDepth);
    Set<String> crawl(String executionUuid, String startUrl);
}