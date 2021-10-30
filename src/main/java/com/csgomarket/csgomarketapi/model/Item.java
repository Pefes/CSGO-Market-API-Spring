package com.csgomarket.csgomarketapi.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

public class Item {
    @Id
    public String id;
    public String name;
    public String iconUrl;
    public String type;
    public String exterior;
    public String rarity;
    public String rarityColor;
    public float price;
    public boolean purchasable;
    public boolean openable;
    public List<Map<String, String>> content;

    public Item() {}
}
