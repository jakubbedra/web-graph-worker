package com.konfyrm.webgraphworker.service;

import java.util.Map;
import java.util.Set;

public interface RequestProcessingService {
    Map<String, Set<String>> dupa(String startUrl, int maxVisitedNodes);
}
