package com.csgomarket.csgomarketapi.payload.request.authentication;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;

    private String password;
}
