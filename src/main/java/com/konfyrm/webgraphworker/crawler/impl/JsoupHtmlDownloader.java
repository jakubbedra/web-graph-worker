package com.konfyrm.webgraphworker.crawler.impl;

import com.konfyrm.webgraphworker.crawler.HtmlDownloader;
import com.konfyrm.webgraphworker.crawler.UrlUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class JsoupHtmlDownloader implements HtmlDownloader {

    private static final Logger LOGGER = LogManager.getLogger(JsoupUrlManager.class);

    @Value("application.crawler.download-path")
    private String downloadPath;

    @Override
    public void downloadHtmlDocument(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();
            String fileName = UrlUtils.getWithoutHttp(url).replace("/", "-");

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(downloadPath + "/" + fileName + ".html"))) {
                bufferedWriter.write(document.outerHtml());
            }
        } catch (IOException e) {
            LOGGER.warn("Error downloading HTML document from " + url + ": " + e.getMessage());
        }
    }

}