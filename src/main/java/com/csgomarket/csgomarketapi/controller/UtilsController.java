package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UtilsController {

    @Autowired
    private UtilsService utilsService;

    @GetMapping("getAutocompleteOptions")
    public ApiResponse<?> getAutocompleteOptions(@RequestParam String property) {
        return utilsService.getAutocompleteOptions(property);
    }
}
