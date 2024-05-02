package com.konfyrm.webgraphworker.domain.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UrlVisitResult {
    private String startUrl;
    private Map<String, Set<String>> neighbours;
}

