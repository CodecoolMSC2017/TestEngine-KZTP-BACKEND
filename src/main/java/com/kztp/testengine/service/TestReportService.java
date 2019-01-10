package com.kztp.testengine.service;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.TestReport;
import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.TestReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class TestReportService {
    @Autowired
    private TestReportsRepository testReportsRepository;

    @Autowired
    private UserService userService;

    public Page<TestReport> getAllReportedTest(Pageable pageable){
        return testReportsRepository.findAll(pageable);
    }

    public Page<TestReport> getAllReportedTestByTest(Test test, Pageable pageable){
        return testReportsRepository.findAllByReportedTestAndSolvedFalse(test,pageable);
    }

    public Page<TestReport> getAllReportedTestByUser(User user, Pageable pageable){
        return testReportsRepository.findAllByReporterAndSolvedFalse(user,pageable);
    }

    public Page<TestReport> getAllReportedTestByForLoggedUser(Pageable pageable){
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return testReportsRepository.findAllByReporterAndSolvedFalse(user,pageable);
    }

    public String reportTest(Test test,String description,String userdescription) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(testReportsRepository.existsByReporterAndReportedTest(user,test)) {
            TestReport existingReport = testReportsRepository.findByReporterAndReportedTest(user,test);
            existingReport.setDescription(description);
            existingReport.setUserDescription(userdescription);
            existingReport.setSolved(false);
            testReportsRepository.save(existingReport);
            return "Description updated on report.";
        }
        else {
            TestReport testReport = new TestReport();
            testReport.setReporter(user);
            testReport.setDescription(description);
            testReport.setUserDescription(userdescription);
            testReport.setReportedTest(test);
            testReportsRepository.save(testReport);


        }

        return "Test reported!";
    }

    public void resolveReport(int reportId) {
        TestReport report =testReportsRepository.findById(reportId).get();
        report.setSolved(true);
        testReportsRepository.save(report);
    }
}
