package com.konfyrm.webgraphworker.crawler;

import java.util.List;
import java.util.Map;

public interface WebCrawler {
    Map<String, List<String>> crawl(String startUrl, int maxDepth);
}