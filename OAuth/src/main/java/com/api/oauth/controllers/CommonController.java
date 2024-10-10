package com.api.oauth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping("/public")
    public String homePage() {
        return "Hello from Spring boot app";
    }

    @GetMapping("/private")
    public String privateRoute() {
        return "Private Route";
    }
}