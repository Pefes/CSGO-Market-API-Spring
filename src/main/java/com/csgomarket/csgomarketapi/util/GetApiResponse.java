package com.csgomarket.csgomarketapi.util;

import com.csgomarket.csgomarketapi.payload.response.ApiResponse;

public class GetApiResponse {

    public static <T> ApiResponse<T> getApiResponse(String status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
