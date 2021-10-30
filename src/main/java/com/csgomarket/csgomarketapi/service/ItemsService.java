package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Item> getMarketItems() {
        return mongoTemplate.findAll(Item.class, "items");
    }
}
