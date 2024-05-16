package com.konfyrm.webgraphworker.service;

import com.konfyrm.webgraphworker.domain.message.UrlNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RequestProcessingService {
    Map<String, Set<String>> processRequest(String startUrl, int maxVisitedNodes);
    List<UrlNode> processRequests(String executionUuid, List<String> urls);
}

