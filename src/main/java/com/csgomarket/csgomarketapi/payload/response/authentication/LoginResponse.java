package com.csgomarket.csgomarketapi.payload.response.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String accessToken;

    private String expiresIn;

    private UserData userData;
}
