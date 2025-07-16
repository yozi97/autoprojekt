package com.zijad.autoprojekt.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserProfileResponse {

    private String email;
    private int numPosts;
    private LocalDateTime regDate;
    private boolean isEmailVerified;
}
