package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.dto.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(String email);
    void verifyEmail(String email);
}
