package com.kztp.testengine.repository;

import com.kztp.testengine.model.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestReportsRepository extends JpaRepository<TestReport,Integer> {

}
