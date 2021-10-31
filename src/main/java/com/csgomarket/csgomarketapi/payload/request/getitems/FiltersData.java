package com.csgomarket.csgomarketapi.payload.request.getitems;

import lombok.Data;

@Data
public class FiltersData {

    private String name;

    private String type;

    private String rarity;

    private String exterior;

    private boolean openable;

    private SortingData sorting;
}
