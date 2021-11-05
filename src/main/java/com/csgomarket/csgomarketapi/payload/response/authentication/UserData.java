package com.csgomarket.csgomarketapi.payload.response.authentication;

import com.csgomarket.csgomarketapi.model.user.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserData {

    private String username;

    private BigDecimal cash;

    private UserSettings userSettings;
}
