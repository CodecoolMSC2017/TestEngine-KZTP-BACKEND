package com.kztp.testengine.repository;

import com.kztp.testengine.model.PoolPoint;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoolPointRepository extends JpaRepository<PoolPoint,Integer>{
    boolean existsByVoterAndTest(User voter, Test test);
    PoolPoint getById(int id);
    PoolPoint getByTestAndVoter(Test test,User voter);
    List<PoolPoint> getAllByTest(Test test);
    List<PoolPoint> getAllByTestAndVote(Test test,Integer vote);
    void deleteById(int id);
}
