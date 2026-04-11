package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("url-shortener")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("shorten")
    public String shortenUrl(@RequestBody String longUrl) {
        return "";
        //generate shortcode, save in db and return shortcode;
    }
}
