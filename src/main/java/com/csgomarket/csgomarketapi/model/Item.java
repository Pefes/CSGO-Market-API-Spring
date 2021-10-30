package com.csgomarket.csgomarketapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import java.util.List;

@Data
public class Item {
    @Id
    private String id;
    private String name;
    private String iconUrl;
    private String type;
    private String exterior;
    private String rarity;
    private String rarityColor;
    private float price;
    private boolean purchasable;
    private boolean openable;
    private List<ItemContent> content;
}
