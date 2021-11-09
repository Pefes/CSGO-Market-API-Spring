package com.csgomarket.csgomarketapi.model.lastopened;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.LAST_OPENED_COLLECTION;

@Data
@Document(collection = LAST_OPENED_COLLECTION)
public class LastOpened {

    @Id
    public String id;

    public String containerName;

    public String containerIconUrl;

    public String itemName;

    public String itemIconUrl;

    public String itemType;

    public String itemExterior;

    public String itemRarity;

    public String itemRarityColor;

    public BigDecimal itemPrice;

    public String ownerUsername;

    public LocalDate openedDate;
}
