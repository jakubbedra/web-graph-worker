package com.konfyrm.webgraphworker.domain.message;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UrlVisitResult {
    private String executionUuid;
    private List<UrlNode> nodes;
}