package com.konfyrm.webgraphworker.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class JsoupWebCrawler implements WebCrawler {

    // todo: add log4j logger
    // todo: respect robots.txt!!!!

    public Map<String, List<String>> crawl(String startUrl, int maxDepth) {
        Map<String, List<String>> visitedUrls = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startUrl);

        int currentDepth = 0;
        while (!queue.isEmpty()) {
            String currentUrl = queue.poll();
            System.out.println("Crawling: " + currentUrl);

            try {
                Document document = Jsoup.connect(currentUrl).get();
                Elements links = document.select("a[href]");

                List<String> neighbours = links.stream()
                        .map(e -> e.absUrl("href"))
                        .toList();
                visitedUrls.put(currentUrl, neighbours);
                if (currentDepth < maxDepth) {
                    for (String link : neighbours) {
                        if (!visitedUrls.containsKey(link)) {
                            queue.add(link);
                        }
                    }
                    currentDepth++;
                }
            } catch (IOException e) {
                System.err.println("Error crawling " + currentUrl + ": " + e.getMessage());
            }
        }
        return visitedUrls;
    }

}
