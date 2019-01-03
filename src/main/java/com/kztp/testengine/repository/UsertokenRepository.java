package com.kztp.testengine.repository;

import com.kztp.testengine.model.User;
import com.kztp.testengine.model.Usertoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsertokenRepository extends JpaRepository<Usertoken,Integer> {
    Usertoken findByToken(String token);
    Usertoken findByUser(User user);
}
