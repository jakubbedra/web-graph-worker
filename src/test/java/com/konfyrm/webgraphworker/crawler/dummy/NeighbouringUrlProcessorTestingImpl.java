package com.konfyrm.webgraphworker.crawler.dummy;

import com.konfyrm.webgraphworker.crawler.NeighbouringUrlProcessor;

import java.util.Set;

public class NeighbouringUrlProcessorTestingImpl implements NeighbouringUrlProcessor {
    @Override
    public Set<String> processNeighbouringUrls(String currentUrl, Set<String> neighbours) {
        return neighbours;
    }
}