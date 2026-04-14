package com.example.url_shortener.service;

import org.springframework.stereotype.Service;

@Service
public class ShortCodeGenerator {

    // Base62 characters
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String convertToBase62(long num) {
        StringBuilder sb = new StringBuilder();

        while(num > 0) {
            int remainder = (int)num % 62;
            sb.append(BASE62.charAt(remainder));
            num = num / 62;
        }
        return sb.reverse().toString();
    }
}
