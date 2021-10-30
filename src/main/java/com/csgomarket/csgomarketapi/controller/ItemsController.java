package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.model.Item;
import com.csgomarket.csgomarketapi.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    @GetMapping("getMarketItems")
    public List<Item> getMarketItems() {
        return itemsService.getMarketItems();
    }
}
