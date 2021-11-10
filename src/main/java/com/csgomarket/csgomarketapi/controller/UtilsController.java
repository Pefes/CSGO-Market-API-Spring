package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.payload.request.setusersettings.SetUserSettingsRequest;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api")
public class UtilsController {

    @Autowired
    private UtilsService utilsService;

    @GetMapping("getAutocompleteOptions")
    public ApiResponse<List<String>> getAutocompleteOptions(@RequestParam String property) {
        return utilsService.getAutocompleteOptions(property);
    }

    @PostMapping("setUserSettings")
    public ApiResponse<?> setUserSettings(@RequestBody SetUserSettingsRequest request) {
        return utilsService.setUserSettings(request.getUserSettings());
    }
}
