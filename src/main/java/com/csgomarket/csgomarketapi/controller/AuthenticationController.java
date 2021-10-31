package com.csgomarket.csgomarketapi.controller;

import com.csgomarket.csgomarketapi.payload.request.authentication.AuthenticationRequest;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.payload.response.LoginResponse;
import com.csgomarket.csgomarketapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("register")
    public ApiResponse<?> register(@RequestBody AuthenticationRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("login")
    public ApiResponse<LoginResponse> login(@RequestBody AuthenticationRequest request) {
        return authenticationService.login(request);
    }
}
