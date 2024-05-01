package com.konfyrm.webgraphworker.domain.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UrlVisitRequest {
    private String url;
}