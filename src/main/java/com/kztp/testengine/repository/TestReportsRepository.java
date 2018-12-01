package com.kztp.testengine.repository;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.TestReport;
import com.kztp.testengine.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestReportsRepository extends JpaRepository<TestReport,Integer> {
    Page<TestReport> findAllByReporter(User reporter, Pageable pageable);
    Page<TestReport> findAllByReportedTest(Test test, Pageable pageable);
    Page<TestReport> findAll(Pageable pageable);
}
