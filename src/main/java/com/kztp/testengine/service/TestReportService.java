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
        return testReportsRepository.findAllByReportedTest(test,pageable);
    }

    public Page<TestReport> getAllReportedTestByUser(User user, Pageable pageable){
        return testReportsRepository.findAllByReporter(user,pageable);
    }

    public Page<TestReport> getAllReportedTestByForLoggedUser(Pageable pageable){
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return testReportsRepository.findAllByReporter(user,pageable);
    }
}
