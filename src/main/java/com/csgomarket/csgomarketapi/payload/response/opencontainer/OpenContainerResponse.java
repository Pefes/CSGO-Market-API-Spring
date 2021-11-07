package com.csgomarket.csgomarketapi.payload.response.opencontainer;

import com.csgomarket.csgomarketapi.model.item.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenContainerResponse {

    private Item drawnItem;
}
