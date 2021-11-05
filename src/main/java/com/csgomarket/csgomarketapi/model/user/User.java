package com.csgomarket.csgomarketapi.model.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.util.List;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.USERS_COLLECTION;

@Data
@Builder
@Document(collection = USERS_COLLECTION)
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private BigDecimal cash;

    private UserSettings userSettings;

    @Field(targetType = FieldType.OBJECT_ID)
    private List<String> ownedItems;
}
