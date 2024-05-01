package com.konfyrm.webgraphworker.domain.message;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UrlVisitResult {
    private String url;
    private List<String> neighbours;
}
