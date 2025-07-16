package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String email);
    void verifyEmail(String email);
}
