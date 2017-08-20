package com.whiuk.philip.perfect.message.html;


import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


public class HtmlHelper {
    public static String extractText(String html) {
        return Jsoup.clean(html, Whitelist.none());
    }
}
