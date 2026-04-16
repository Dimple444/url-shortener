package com.example.url_shortener.dto;

import com.example.url_shortener.util.ValidUrl;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ShortenUrlRequest(

        @ValidUrl
        String longUrl,

        @Min(value = 1, message = "Expiry must be atleast 1 day")
        @Max(value = 365, message = "Expiry cannot exceed 1 year")
        Integer expiryDays
) {

    // Compact Constructor - without parenthesis () - Runs as soon as the JSON is mapped to the Record
    public ShortenUrlRequest {
        if (longUrl != null && !longUrl.isEmpty()) {
            String lower = longUrl.toLowerCase().trim();
            if(!lower.startsWith("http://") && !lower.startsWith("https://")) {
                longUrl = "https://" + longUrl.trim();
            }
        }
        // No need for this.longUrl = longUrl; Java does it automatically at the end of this block
    }
}
