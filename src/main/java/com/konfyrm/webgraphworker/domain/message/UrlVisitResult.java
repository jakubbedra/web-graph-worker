package com.konfyrm.webgraphworker.domain.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UrlVisitResult {
    private String startUrl;
    private Map<String, List<String>> neighbours;
}

