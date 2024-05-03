package com.konfyrm.webgraphworker.domain.message;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UrlVisitRequest {
    private String executionUuid;
    private String url;
    private int maxVisitedNodes;
}
