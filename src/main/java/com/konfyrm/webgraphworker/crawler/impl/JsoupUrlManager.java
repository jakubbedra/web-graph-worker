package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.UrlManager;
import com.konfyrm.webgraphworker.crawler.UrlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JsoupUrlManager implements UrlManager {

    private static final Logger LOGGER = LogManager.getLogger(JsoupUrlManager.class);

    public static final String DISALLOW = "Disallow: ";
    private static String ROBOTS_FILE = "robots.txt";

    @Override
    public Set<String> getNeighbouringUrls(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");

            return links.stream()
                    .map(e -> e.absUrl("href"))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOGGER.info("Error crawling " + url + ": " + e.getMessage());
            return Collections.emptySet();
        }
    }

    @Override
    public Set<String> getDisallowedPatterns(java.lang.String url) {
        try {
            java.lang.String baseUrl = UrlUtils.getBaseUrl(url);
            java.lang.String robotsUrl = baseUrl + "/" + ROBOTS_FILE;
            Connection connection = Jsoup.connect(robotsUrl);
            Document document = connection.get();
            java.lang.String[] split = document.text().split("\n");
            return Arrays.stream(split)
                    .filter(s -> s.contains(DISALLOW))
                    .map(s -> s.replace(DISALLOW, ""))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOGGER.info("Error checking robots.txt for URL: " + url);
            return Collections.emptySet();
        }
    }

}