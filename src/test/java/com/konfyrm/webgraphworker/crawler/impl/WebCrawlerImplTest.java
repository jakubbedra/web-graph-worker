package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.HtmlDownloader;
import com.konfyrm.webgraphworker.crawler.NeighbouringUrlProcessor;
import com.konfyrm.webgraphworker.crawler.UrlManager;
import com.konfyrm.webgraphworker.crawler.WebCrawler;
import com.konfyrm.webgraphworker.crawler.dummy.NeighbouringUrlProcessorTestingImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class WebCrawlerImplTest {

    private final UrlManager urlManager;
    private final HtmlDownloader htmlDownloader;
    private final NeighbouringUrlProcessor neighbouringUrlProcessor;
    private final WebCrawler webCrawler;

    public WebCrawlerImplTest() {
        urlManager = Mockito.mock(UrlManager.class);
        htmlDownloader = Mockito.mock(HtmlDownloader.class);
        neighbouringUrlProcessor = new NeighbouringUrlProcessorTestingImpl();
        webCrawler = new WebCrawlerImpl(urlManager, htmlDownloader, neighbouringUrlProcessor);

        Mockito.doNothing().when(htmlDownloader).downloadHtmlDocument(any());
    }

    @Test
    public void deprecated_crawl_startUrlOk_someNeighbours() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        int maxVisitedNodes = 1;
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getNeighbouringUrls(Mockito.argThat(argument -> !startUrl.equals(argument))))
                .thenReturn(Collections.emptySet());
        Map<String, Set<String>> expectedNodes = Map.of(
                startUrl, neighbours
        );

        Map<String, Set<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void deprecated_crawl_startUrlOkMaxVisitedTwoNodes() {
        String startUrl = "https://www.jandaciuk.pl";
        String firstNeighbourUrl = "https://www.jandaciuk.pl/papaj";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        Set<String> neighbours2 = sampleValidNeighboursForStartUrlNeighbour();

        int maxVisitedNodes = 2;
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getNeighbouringUrls(Mockito.eq(firstNeighbourUrl))).thenReturn(neighbours2);

        Map<String, Set<String>> expectedNodes = Map.of(
                startUrl, neighbours,
                firstNeighbourUrl, neighbours2
        );

        Map<String, Set<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void deprecated_crawl_secondVisitedNodeHasStartUrlNeighbour() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighbourForStartUrl();
        String firstNeighbourUrl = "https://www.jandaciuk.pl/papaj";
        Set<String> neighbours2 = sampleValidNeighboursWithStartUrlNeighbour();
        String thirdUrl = "https://www.jandaciuk.pl/papaj/2137";
        int maxVisitedNodes = 3;
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getNeighbouringUrls(Mockito.eq(firstNeighbourUrl))).thenReturn(neighbours2);
        when(urlManager.getNeighbouringUrls(Mockito.eq(thirdUrl))).thenReturn(Collections.emptySet());
        Map<String, Set<String>> expectedNodes = Map.of(
                startUrl, neighbours,
                firstNeighbourUrl, neighbours2,
                thirdUrl, Collections.emptySet()
        );

        Map<String, Set<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void deprecated_crawl_startUrlDisallowed_emptyMapReturned() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        int maxVisitedNodes = 1;
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getDisallowedPatterns(startUrl)).thenReturn(Set.of(startUrl));
        Map<String, Set<String>> expectedNodes = Collections.emptyMap();

        Map<String, Set<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void deprecated_crawl_neighbourDisallowed() {
        String startUrl = "https://www.jandaciuk.pl";
        String firstNeighbourUrl = "https://www.jandaciuk.pl/papaj";
        String firstAllowedNeighbourUrl = "https://www.jandaciuk.pl/javashit";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        Set<String> neighbours2 = sampleValidNeighboursForStartUrlNeighbour();

        int maxVisitedNodes = 2;
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getNeighbouringUrls(Mockito.eq(firstNeighbourUrl))).thenReturn(neighbours2);
        when(urlManager.getDisallowedPatterns(startUrl)).thenReturn(Set.of("/papaj"));

        Map<String, Set<String>> expectedNodes = Map.of(
                startUrl, neighbours,
                firstAllowedNeighbourUrl, Collections.emptySet()
        );

        Map<String, Set<String>> nodes = webCrawler.crawl(startUrl, maxVisitedNodes);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void crawl_startUrlOk_someNeighbours() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getNeighbouringUrls(Mockito.argThat(argument -> !startUrl.equals(argument))))
                .thenReturn(Collections.emptySet());
        Set<String> expectedNodes = sampleValidNeighboursForStartUrl();

        Set<String> nodes = webCrawler.crawl(startUrl);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void crawl_startUrlDisallowed_emptyMapReturned() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getDisallowedPatterns(startUrl)).thenReturn(Set.of(startUrl));
        Set<String> expectedNodes = Collections.emptySet();

        Set<String> nodes = webCrawler.crawl(startUrl);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    @Test
    public void crawl_neighbourDisallowed() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrl();
        when(urlManager.getNeighbouringUrls(Mockito.eq(startUrl))).thenReturn(neighbours);
        when(urlManager.getDisallowedPatterns(startUrl)).thenReturn(Set.of("/papaj"));
        Set<String> expectedNodes = sampleValidNeighboursForStartUrlWithoutDisallowed();

        Set<String> nodes = webCrawler.crawl(startUrl);

        Assertions.assertEquals(expectedNodes, nodes);
    }

    private Set<String> sampleValidNeighboursForStartUrl() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/papaj",
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithoutDisallowed() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighbourForStartUrl() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/papaj"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/papajak",
                "https://www.jandaciuk.pl/papaj/2137",
                "https://www.jandaciuk.pl/papaj/ak",
                "https://www.jandaciuk.pl"
        ));
    }

    private Set<String> sampleValidNeighboursWithStartUrlNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl",
                "https://www.jandaciuk.pl/papaj/2137",
                "https://www.jandaciuk.pl/papaj/ak"
        ));
    }

}