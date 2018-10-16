package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test,Integer> {
    Test findById(int id);
    Page<Test> findByLiveTrue(Pageable pageable);
    Page<Test> findByLiveFalse(Pageable pageable);
    List<Test> findByCreatorAndLiveTrue(User creator);
}
