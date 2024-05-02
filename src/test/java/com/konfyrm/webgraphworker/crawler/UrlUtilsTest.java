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

}