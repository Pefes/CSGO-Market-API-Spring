package com.csgomarket.csgomarketapi.model.item;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.ITEMS_COLLECTION;

@Data
@Document(collection = ITEMS_COLLECTION)
public class Item {

    @Id
    private String id;

    private String name;

    private String iconUrl;

    private String type;

    private String exterior;

    private String rarity;

    private String rarityColor;

    private BigDecimal price;

    private boolean purchasable;

    private boolean openable;

    private List<ItemContent> content;
}
