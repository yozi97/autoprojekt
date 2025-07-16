package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.dto.auth.UserProfileResponse;
import com.zijad.autoprojekt.model.User;
import com.zijad.autoprojekt.repository.CarRepository;
import com.zijad.autoprojekt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Override
    public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        int numPosts = carRepository.countByUser(user);

        return UserProfileResponse.builder()
                .email(user.getEmail())
                .numPosts(numPosts)
                .regDate(user.getCreatedAt())
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    public void verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEmailVerified(true);
        userRepository.save(user);
    }

}
