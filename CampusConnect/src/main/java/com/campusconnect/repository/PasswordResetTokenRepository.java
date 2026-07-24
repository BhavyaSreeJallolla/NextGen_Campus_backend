package com.campusconnect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusconnect.entity.PasswordResetToken;
import com.campusconnect.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUser(User user);

}