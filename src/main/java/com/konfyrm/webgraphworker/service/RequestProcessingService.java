package com.konfyrm.webgraphworker.service;

import com.konfyrm.webgraphworker.domain.message.UrlNode;

import java.util.List;

public interface RequestProcessingService {
    List<UrlNode> processRequests(String executionUuid, List<String> urls, boolean downloadFiles);
}

