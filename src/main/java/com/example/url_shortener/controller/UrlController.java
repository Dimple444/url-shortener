package com.example.url_shortener.controller;

import com.example.url_shortener.dto.ShortenUrlRequest;
import com.example.url_shortener.dto.ShortenUrlResponse;
import com.example.url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    /**
     * Creation Endpoint
     * Returns 201 Created and the short code.
     * Note: If the code enters this method, the RequestBody request is already valid.
     */
    @PostMapping("shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        ShortenUrlResponse response = urlService.shortenUrl(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
                .header("X-Content-Type-Options", "nosniff")
                .header("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate")
                .build();

    }
}
