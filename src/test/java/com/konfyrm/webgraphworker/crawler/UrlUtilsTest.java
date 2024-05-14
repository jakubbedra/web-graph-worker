package com.konfyrm.webgraphworker.crawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlUtilsTest {

    @Test
    public void containsPattern_urlContainsPattern() {
        String url = "https://www.jandaciuk.pl/typo3/1000";
        String pattern = "/typo3/";

        boolean result = UrlUtils.containsPattern(url, pattern);

        Assertions.assertTrue(result);
    }

    @Test
    public void containsPattern_urlContainsPatternWithWildcard() {
        String url = "https://www.jandaciuk.pl/papaj/1000";
        String pattern = "/*/1000";

        boolean result = UrlUtils.containsPattern(url, pattern);

        Assertions.assertTrue(result);
    }

    @Test
    public void containsPattern_urlContainsPatternWithWholeUrl() {
        String url = "https://www.jandaciuk.pl/papaj/1000";
        String pattern = "https://www.jandaciuk.pl/papaj/1000";

        boolean result = UrlUtils.containsPattern(url, pattern);

        Assertions.assertTrue(result);
    }

    @Test
    public void containsPattern_urlDoesNotContainPattern() {
        String url = "https://www.jandaciuk.pl/papiesz/1000";
        String pattern = "/typo3/";

        boolean result = UrlUtils.containsPattern(url, pattern);

        Assertions.assertFalse(result);
    }

    @Test
    public void extractHost_http() {
        String url = "http://www.jandaciuk.pl";
        String expected = "www.jandaciuk.pl";

        String result = UrlUtils.extractHost(url);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void extractHost_https() {
        String url = "https://www.jandaciuk.pl";
        String expected = "www.jandaciuk.pl";

        String result = UrlUtils.extractHost(url);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void extractHost_httpsMoreComplexUrl() {
        String url = "https://www.jandaciuk.pl/about/javashit";
        String expected = "www.jandaciuk.pl";

        String result = UrlUtils.extractHost(url);

        Assertions.assertEquals(result, expected);
    }

    @Test
    public void extractHost_socialMediaRef() {
        String url = " https://www.threads.net/intent/post?text=https://www.tum.de/en/news-and-events/all-news/press-releases/details/hoehere-impfbereitschaft-durch-einfache-risikovergleiche";
        String expected = "www.threads.net";

        String result = UrlUtils.extractHost(url);

        Assertions.assertEquals(result, expected);
    }

}