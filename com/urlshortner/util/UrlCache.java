package com.urlshortner.util;

import java.util.HashMap;
import java.util.Map;

public class UrlCache {

    private static Map<String, String> cache = new HashMap<>();

    public static void put(String shortCode, String longUrl) {
        cache.put(shortCode, longUrl);
    }

    public static String get(String shortCode) {
        return cache.get(shortCode);
    }
}