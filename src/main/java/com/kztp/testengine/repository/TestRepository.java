package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test,Integer> {
    Test findById(int id);
    Page<Test> findByLiveTrueAndEnabledTrue(Pageable pageable);
    Page<Test> findByLiveFalseAndEnabledTrue(Pageable pageable);
    Page<Test> findByCreatorAndLiveTrueAndEnabledTrue(User creator,Pageable pageable);
    Page<Test> findByCreatorAndLiveFalseAndEnabledTrue(User creator,Pageable pageable);
    @Query(value="SELECT * FROM tests WHERE live=?1 AND LOWER(title) LIKE LOWER(?2) AND rating BETWEEN ?3 AND ?4 AND price BETWEEN ?5 AND ?6 and enabled=true",nativeQuery = true)
    Page<Test> findAllByParameter(boolean live,String title,int ratingMin,int ratingMax,int priceMin,int priceMax,Pageable pageable);
}
