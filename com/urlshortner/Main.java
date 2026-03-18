package com.urlshortner;

import com.sun.net.httpserver.HttpServer;
import com.urlshortner.repository.UrlRepository;
import com.urlshortner.util.Base62Encoder;
import com.urlshortner.util.UrlCache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws IOException {

        UrlRepository repo = new UrlRepository();
        AtomicInteger counter = new AtomicInteger(1);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint to create short URL
        server.createContext("/shorten", exchange -> {

            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

                String query = exchange.getRequestURI().getQuery();

                if (query == null || !query.startsWith("url=")) {
                    String response = "Usage: /shorten?url=YOUR_URL";
                    exchange.sendResponseHeaders(400, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.close();
                    return;
                }

                String longUrl = query.substring(4);

                int id = counter.getAndIncrement();
                String shortCode = Base62Encoder.encode(id);

                repo.save(shortCode, longUrl);

                String response = "Short URL: http://localhost:8080/" + shortCode;

                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            }
        });

        // Redirect handler
        server.createContext("/", exchange -> {

            String path = exchange.getRequestURI().getPath();

            if (path.length() <= 1) {
                String response = "URL Shortener Running";
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
                return;
            }

            String shortCode = path.substring(1);

            // Step 1: check cache
            String longUrl = UrlCache.get(shortCode);

            // Step 2: if not in cache, check database
            if (longUrl == null) {
                longUrl = repo.get(shortCode);

                if (longUrl != null) {
                    UrlCache.put(shortCode, longUrl);
                }
            }

            if (longUrl == null) {
                String response = "Short URL not found";
                exchange.sendResponseHeaders(404, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
                return;
            }
            repo.incrementClicks(shortCode);

            exchange.getResponseHeaders().add("Location", longUrl);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        });

        server.start();

        System.out.println("Server started at http://localhost:8080");
    }
}