package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import com.kztp.testengine.model.UsersTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersTestRepository extends JpaRepository<UsersTest,Integer>{

    UsersTest findById(int id);
    List<UsersTest> findByUser(User user);
    List<UsersTest> findByTest(Test test);
    @Query(value="select cast(AVG(percentage) as numeric(5,2)) from userstests where test_id=?",nativeQuery = true)
    Float getAveragePercentageOfUsersTestsByTest(int testId);
    boolean existsByUserAndTest(User user,Test test);
}
