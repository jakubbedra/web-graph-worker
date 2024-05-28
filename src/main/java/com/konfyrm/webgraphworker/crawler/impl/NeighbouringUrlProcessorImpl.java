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
                .filter(url -> !url.endsWith(".pdf"))
                .filter(url -> !url.contains("tel:+"))
                .map(UrlUtils::trim)
                .filter(nextUrl -> UrlUtils.extractHost(nextUrl).equals(UrlUtils.extractHost(currentUrl)))
                .filter(nextUrl -> !nextUrl.equals(currentUrl))
                .collect(Collectors.toSet());
    }
}
