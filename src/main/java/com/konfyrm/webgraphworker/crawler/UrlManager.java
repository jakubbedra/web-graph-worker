package com.konfyrm.webgraphworker.crawler;

import java.util.List;
import java.util.Set;

public interface UrlManager {
    List<String> getNeighbouringUrls(String url);
    Set<String> getDisallowedPatterns(String url);
}