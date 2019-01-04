package com.kztp.testengine.repository;

import com.kztp.testengine.model.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken,Integer> {
    PasswordToken findByToken(String token);
}
