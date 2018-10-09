package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Test,Integer> {
}
