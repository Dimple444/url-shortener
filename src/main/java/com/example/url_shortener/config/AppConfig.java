package com.example.url_shortener.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(value = "app.url-shortener")
@Validated
public class AppConfig {

    @NotBlank
    private String baseUrl;

    @Min(1)
    private int defaultExpiryMonths;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getDefaultExpiryMonths() {
        return defaultExpiryMonths;
    }

    public void setDefaultExpiryMonths(int defaultExpiryMonths) {
        this.defaultExpiryMonths = defaultExpiryMonths;
    }
}
