package com.example.project3.repository;

import com.example.project3.entity.RefreshToken;
import com.example.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}