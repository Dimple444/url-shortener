package com.example.url_shortener.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlValidator {

    /**
     * Validates if a string is a properly formatted absolute URL.
     * 1. Checks for basic syntax errors.
     * 2. Ensures the protocol is either HTTP or HTTPS.
     * 3. Ensures there is a valid host (domain).
     */
    public static boolean isValid(String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return false;
        }

        try {
            // Use URI for syntax checking (faster, no DNS side effects)
            URI uri = new URI(urlString);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                return false;
            }
            if(uri.getHost() == null || uri.getHost().isEmpty()) {
                return false;
            }
            uri.toURL();
            return true;
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }
}
