package com.example.url_shortener.controller;

import com.example.url_shortener.exception.InvalidUrlException;
import com.example.url_shortener.service.UrlService;
import com.example.url_shortener.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    @Autowired
    private UrlService urlService;

    /**
     * Creation Endpoint
     * Returns 201 Created and the short code.
     */
    @PostMapping("shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String longUrl) {
        String cleanedUrl = longUrl.trim();
        if(!cleanedUrl.startsWith("http://") && !cleanedUrl.startsWith("https://")) {
            cleanedUrl = "https://" + cleanedUrl;
        }

        if(!UrlValidator.isValid(cleanedUrl)) {
            throw new InvalidUrlException("The provided URL is invalid" + cleanedUrl);
        }

        String shortCode = urlService.shortenUrl(cleanedUrl);
        return new ResponseEntity<>(shortCode, HttpStatus.CREATED);
    }

    /**
     * Redirect Endpoint
     * Users visit: yourdomain.com/api/v1/urls/{shortCode}
     * Returns 302 Found (Temporary Redirect) to the destination.
     * We use 302 (Temporary) instead of 301 (Permanent) because it forces the browser to check
     * with our server on every click, allowing us to track analytics.
     */
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable("shortCode") String shortCode) {
        // If this fails, the GlobalExceptionHandler will catch it
        String longUrl = urlService.resolveUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();

    }
}
