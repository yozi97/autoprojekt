package com.zijad.autoprojekt.controller;

import com.zijad.autoprojekt.dto.auth.UserProfileResponse;
import com.zijad.autoprojekt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getProfile(email));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> verifyEmail(Authentication authentication) {
        String email = authentication.getName();
        userService.verifyEmail(email);
        return ResponseEntity.ok("Email verified successfully.");
    }

}
