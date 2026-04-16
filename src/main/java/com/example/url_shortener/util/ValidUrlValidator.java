package com.example.url_shortener.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.net.URISyntaxException;

public class ValidUrlValidator implements ConstraintValidator<ValidUrl, String> {

    /**
     * Validates if a string is a properly formatted absolute URL.
     * 1. Checks for basic syntax errors.
     * 2. Ensures the protocol is either HTTP or HTTPS.
     * 3. Ensures there is a valid host (domain).
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            // Use URI for syntax checking (faster, no DNS side effects)
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                return false;
            }
            if(uri.getHost() == null || uri.getHost().isBlank()) {
                return false;
            }
            return true;
        } catch (URISyntaxException | IllegalArgumentException e) {
            return false;
        }
    }
}
