package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.NeighbouringUrlProcessor;
import com.konfyrm.webgraphworker.crawler.UrlUtils;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NeighbouringUrlProcessorImpl implements NeighbouringUrlProcessor {
    @Override
    public Set<String> processNeighbouringUrls(String currentUrl, Set<String> neighbours) {
        return neighbours.stream()
                .filter(u -> !u.contains("#"))
                .map(UrlUtils::trim)
                .filter(nextUrl -> nextUrl.contains(UrlUtils.extractHost(currentUrl)))
                .filter(nextUrl -> !nextUrl.equals(currentUrl))
                .collect(Collectors.toSet());
    }
}
