package com.csgomarket.csgomarketapi.payload.request.getitems;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetItemsRequest {
    private FiltersData filtersData;
    private PaginatorData paginatorData;
}
