package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.payload.request.getitems.GetItemsRequest;
import com.csgomarket.csgomarketapi.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    @PostMapping("getMarketItems")
    public List<Item> getMarketItems(@RequestBody GetItemsRequest requestBody) {
        return itemsService.getMarketItems(requestBody);
    }
}
