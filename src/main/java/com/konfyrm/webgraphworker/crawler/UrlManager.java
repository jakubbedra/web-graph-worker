package com.konfyrm.webgraphworker.crawler;

import java.util.Set;

public interface UrlManager {
    Set<String> getNeighbouringUrls(String url);
    Set<String> getDisallowedPatterns(String url);
}