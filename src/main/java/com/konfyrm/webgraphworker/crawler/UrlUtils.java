package com.konfyrm.webgraphworker.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtils {

    private UrlUtils() {}

    public static boolean containsPattern(String url, String patternString) {
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public static boolean areDuplicate(String url1, String url2) {
        return url1.endsWith("/") && url1.contains(url2) || url2.endsWith("/") && url2.contains(url1);
    }

    public static String trim(String url) {
        return url.endsWith("/") ? url.substring(0, url.length()-1) : url;
    }

    public static String getBaseUrl(String url) {
        return "https://" + extractHost(url);
    }

    public static String getWithoutHttp(String url) {
        if (url.contains("https://")) {
            return url.replace("https://", "") ;
        }
        if (url.contains("http://")) {
            return url.replace("http://", "");
        }
        return url;
    }

    public static String extractHost(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getHost();
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + urlString);
            return "";
        }
    }

}