package com.zijad.autoprojekt.controller;

import com.zijad.autoprojekt.model.RefreshToken;
import com.zijad.autoprojekt.dto.auth.LogoutRequest;
import com.zijad.autoprojekt.dto.auth.RefreshTokenRequest;
import com.zijad.autoprojekt.dto.auth.AuthResponse;
import com.zijad.autoprojekt.dto.auth.LoginRequest;
import com.zijad.autoprojekt.dto.auth.RegisterRequest;
import com.zijad.autoprojekt.security.JwtService;
import com.zijad.autoprojekt.service.AuthenticationService;
import com.zijad.autoprojekt.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return ResponseEntity.ok(new AuthResponse(token, request.getRefreshToken()));
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest request) {
        refreshTokenService.deleteByUser(request.getEmail());
        return ResponseEntity.ok("Logged out successfully");
    }

}
