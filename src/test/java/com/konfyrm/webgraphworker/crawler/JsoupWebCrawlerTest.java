package com.konfyrm.webgraphworker.crawler;

import org.junit.jupiter.api.Test;

public class JsoupWebCrawlerTest {

    @Test
    public void dupa() {
        String startUrl = "https://www.tum.de";
        int maxDepth = 1;
        // todo: mock static later on...
        WebCrawler webCrawler = new JsoupWebCrawler();
        webCrawler.crawl(startUrl, maxDepth);

    }

}
