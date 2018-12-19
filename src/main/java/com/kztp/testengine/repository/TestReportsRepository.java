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
    Page<TestReport> findAllByReporterAndSolvedFalse(User reporter, Pageable pageable);
    Page<TestReport> findAllByReportedTestAndSolvedFalse(Test test, Pageable pageable);
    Page<TestReport> findAllBySolvedFalse(Pageable pageable);
    boolean existsByReporterAndReportedTest(User reporter,Test reportedTest);
    TestReport findByReporterAndReportedTest(User reporter,Test reportedTest);
}
