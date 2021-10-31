package com.csgomarket.csgomarketapi.model.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.USERS_COLLECTION;

@Data
@Builder
@Document(collection = USERS_COLLECTION)
public class User {

    @Id
    private String id;

    private String username;

    private String password;

    private long cash;

    private UserSettings userSettings;
}
