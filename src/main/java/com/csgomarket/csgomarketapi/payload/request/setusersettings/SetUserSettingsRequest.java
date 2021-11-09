package com.csgomarket.csgomarketapi.payload.request.setusersettings;

import com.csgomarket.csgomarketapi.model.user.UserSettings;
import lombok.Data;

@Data
public class SetUserSettingsRequest {

    private UserSettings userSettings;
}
