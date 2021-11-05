package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.payload.request.getitems.GetItemsRequest;
import com.csgomarket.csgomarketapi.payload.request.itemtransaction.ItemTransactionRequest;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.payload.response.getitems.GetItemsResponse;
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
    public ApiResponse<GetItemsResponse> getMarketItems(@RequestBody GetItemsRequest request) {
        return itemsService.getMarketItems(request);
    }

    @PostMapping("getOwnedItems")
    public ApiResponse<GetItemsResponse> getOwnedItems(@RequestBody GetItemsRequest request) {
        return itemsService.getOwnedItems(request);
    }

    @GetMapping("getTryOutItems")
    public ApiResponse<GetItemsResponse> getTryOutItems() {
        return itemsService.getTryOutItems();
    }

    @PostMapping("buyItem")
    public ApiResponse<?> buyItem(@RequestBody ItemTransactionRequest request) {
        return itemsService.buyItem(request.getItemId());
    }

    @PostMapping("sellItem")
    public ApiResponse<?> sellItem(@RequestBody ItemTransactionRequest request) {
        return itemsService.sellItem(request.getItemId());
    }
}
