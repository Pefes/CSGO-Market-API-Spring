package com.csgomarket.csgomarketapi.util;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.model.item.ItemContent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.EXTERIOR_FACTORY_NEW;
import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.TYPE_WEAPON;

public class DrawItem {

    private static final Random random = new Random();

    public static Item draw(List<ItemContent> content) {
        ItemContent drawnItem = content.get(random.nextInt(content.size()));
        return Item.builder()
                .name(drawnItem.getName())
                .iconUrl(drawnItem.getIconUrl())
                .price(BigDecimal.valueOf(random.nextInt(1000)))
                .exterior(EXTERIOR_FACTORY_NEW)
                .rarity(drawnItem.getRarity())
                .rarityColor(drawnItem.getRarityColor())
                .purchasable(false)
                .type(TYPE_WEAPON)
                .build();
    }
}
