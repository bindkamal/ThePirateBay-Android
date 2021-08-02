package com.kamalbind.thepiratebaylib.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class WebScrapingHelper {

    public List<String> getAllTheProxyHost(String htmlWebSite) {
        List<String> proxyServers = new ArrayList<>();
        Document document = Jsoup.parse(htmlWebSite);
        for (Element row : document.select("table#proxyList").select("tbody").select("tr")) {
            String url = row.select("td.site").select("a").attr("href");
            if(url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            proxyServers.add(url);
        }
        return proxyServers;
    }

}
