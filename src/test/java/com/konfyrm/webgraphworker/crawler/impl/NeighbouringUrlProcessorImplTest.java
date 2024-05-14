package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.NeighbouringUrlProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NeighbouringUrlProcessorImplTest {

    private final NeighbouringUrlProcessor neighbouringUrlProcessor;

    public NeighbouringUrlProcessorImplTest() {
        neighbouringUrlProcessor = new NeighbouringUrlProcessorImpl();
    }

    @Test
    public void crawl_neighbourDuplicateOfCurrent_someNeighboursReturned() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrlWithDuplicate();
        Set<String> expectedNeighbours = sampleValidNeighboursForStartUrlWithOutDuplicate();

        Set<String> processedNeighbours = neighbouringUrlProcessor.processNeighbouringUrls(startUrl, neighbours);

        Assertions.assertEquals(expectedNeighbours, processedNeighbours);
    }

    @Test
    public void crawl_externalNeighbour() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrlWithExternalNeighbour();
        Set<String> expectedNeighbours = sampleValidNeighboursForStartUrlWithOutExternalNeighbour();

        Set<String> processedNeighbours = neighbouringUrlProcessor.processNeighbouringUrls(startUrl, neighbours);

        Assertions.assertEquals(expectedNeighbours, processedNeighbours);
    }

    @Test
    public void crawl_anchorNeighbour() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleValidNeighboursForStartUrlWithAnchorNeighbour();
        Set<String> expectedNeighbours = sampleValidNeighboursForStartUrlWithOutAnchorNeighbour();

        Set<String> processedNeighbours = neighbouringUrlProcessor.processNeighbouringUrls(startUrl, neighbours);

        Assertions.assertEquals(expectedNeighbours, processedNeighbours);
    }

    @Test
    public void crawl_pdfNeighbour() {
        String startUrl = "https://www.jandaciuk.pl";
        Set<String> neighbours = sampleNeighboursWithPfd();
        Set<String> expectedNeighbours = sampleNeighboursWithoutPfd();

        Set<String> processedNeighbours = neighbouringUrlProcessor.processNeighbouringUrls(startUrl, neighbours);

        Assertions.assertEquals(expectedNeighbours, processedNeighbours);
    }

    private Set<String> sampleNeighboursWithPfd() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/javashit.pdf",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleNeighboursWithoutPfd() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithDuplicate() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/",
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithOutDuplicate() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithExternalNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.studilla.com",
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithOutExternalNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithAnchorNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl#javashit-is-shit",
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

    private Set<String> sampleValidNeighboursForStartUrlWithOutAnchorNeighbour() {
        return new LinkedHashSet<>(List.of(
                "https://www.jandaciuk.pl/javashit",
                "https://www.jandaciuk.pl/faq",
                "https://www.jandaciuk.pl/random/bullshit"
        ));
    }

}
