package com.zijad.autoprojekt.repository;

import com.zijad.autoprojekt.model.RefreshToken;
import com.zijad.autoprojekt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
