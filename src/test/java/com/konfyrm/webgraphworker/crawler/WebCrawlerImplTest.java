package com.konfyrm.webgraphworker.crawler;

import com.konfyrm.webgraphworker.crawler.impl.WebCrawlerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebCrawlerImplTest {

    private final UrlManager urlManager;
    private final HtmlDownloader htmlDownloader;
    private final WebCrawler webCrawler;

    public WebCrawlerImplTest() {
        urlManager = Mockito.mock(UrlManager.class);
        htmlDownloader = Mockito.mock(HtmlDownloader.class);
        webCrawler = new WebCrawlerImpl(urlManager, htmlDownloader);
    }

    @Test
    public void crawl_startUrlOk_someNeighbours() {
        String startUrl = "https://www.jandaciuk.pl";
        List<String> neighbours = sampleValidNeighboursForStartUrl();
        int maxVisitedNodes = 1;
        Mockito.when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        Mockito.when(urlManager.getNeighbouringUrls(Mockito.argThat(argument -> !startUrl.equals(argument))))
                .thenReturn(Collections.emptyList());
        Map<String, List<String>> expectedNodes = Map.of(
                startUrl, neighbours
        );

        Map<String, List<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void crawl_startUrlOkMaxVisitedTwoNodes_someNeighbours() {
//        String startUrl = "https://www.tum.de";
//        int maxVisitedNodes = 3;
//        // todo: mock static later on...
//        WebCrawler webCrawler = new WebCrawlerImpl();
//        Map<String, List<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);
//

    }

    @Test
    public void crawl_startUrlDisallowed_emptyMapReturned() {


    }

    @Test
    public void crawl_startUrlOkNeighbourDisallowed_someNeighboursReturned() {


    }

    @Test
    public void crawl_neighbourDuplicateOfCurrent_someNeighboursReturned() {


    }

    @Test
    public void crawl_externalNeighbour_someNeighboursReturned() {


    }

    // test for robots
    // test for href


    private List<String> sampleValidNeighboursForStartUrl() {
        return List.of(
                "https://www.jandaciuk.pl/papaj",
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        );
    }

}
