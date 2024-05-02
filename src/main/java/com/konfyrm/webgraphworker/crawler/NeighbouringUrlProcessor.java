package com.konfyrm.webgraphworker.crawler;

import java.util.Set;

public interface NeighbouringUrlProcessor {
    public Set<String> processNeighbouringUrls(String currentUrl, Set<String> neighbours);
}
