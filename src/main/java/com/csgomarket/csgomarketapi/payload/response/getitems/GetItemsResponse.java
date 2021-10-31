package com.csgomarket.csgomarketapi.payload.response.getitems;

import com.csgomarket.csgomarketapi.model.item.Item;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetItemsResponse {

    private List<Item> items;

    private long querySize;
}
