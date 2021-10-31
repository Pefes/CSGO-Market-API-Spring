package com.csgomarket.csgomarketapi.payload.response;

import com.csgomarket.csgomarketapi.model.user.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserData {

    private String username;

    private long cash;

    private UserSettings userSettings;
}
