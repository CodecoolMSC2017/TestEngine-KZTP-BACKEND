package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.TestRating;
import com.kztp.testengine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRatingRepository extends JpaRepository<TestRating,Integer> {

    TestRating findByTestAndVoter(Test test,User voter);
    List<TestRating> findAllByTest(Test test);
    boolean existsByTestAndVoter(Test test,User voter);
}
