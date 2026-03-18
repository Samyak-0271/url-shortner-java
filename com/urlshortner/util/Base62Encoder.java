package com.urlshortner.util;

public class Base62Encoder {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode(int number) {
        StringBuilder shortCode = new StringBuilder();

        while (number > 0) {
            int remainder = number % 62;
            shortCode.append(BASE62.charAt(remainder));
            number = number / 62;
        }

        return shortCode.reverse().toString();
    }
}