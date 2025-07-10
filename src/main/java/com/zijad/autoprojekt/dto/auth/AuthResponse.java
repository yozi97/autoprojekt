package com.zijad.autoprojekt.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String token;
    private String refreshToken;

    public AuthResponse(String token, String refreshToken) {

        this.token=token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }
}
