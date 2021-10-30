package com.csgomarket.csgomarketapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemsController {

    @GetMapping("getMarketItems")
    public String getMarketItems() {
        return "string";
    }
}
