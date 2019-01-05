package com.kztp.testengine.service;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.TestDetails;
import com.kztp.testengine.model.User;
import com.kztp.testengine.model.UsersTest;
import com.kztp.testengine.repository.UsersTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class UsersTestService {

    @Autowired
    private UsersTestRepository usersTestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    public List<UsersTest> getCompletedTestsByUser(User user) {
        return usersTestRepository.findByUser(user);
    }

    public List<UsersTest> getLoggedUserCompletedTests(){
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return usersTestRepository.findByUser(user);
    }

    public List<UsersTest> getCompletedTestsByTest(Test test) {
        return usersTestRepository.findByTest(test);
    }

    public TestDetails getTestDetails(int testId) {
        Test test = testService.getTestById(testId);
        int averagePercentage;
        int timesTaken=usersTestRepository.findByTest(test).size();
        int income=test.getPrice()*timesTaken;
        Float percentage = usersTestRepository.getAveragePercentageOfUsersTestsByTest(testId);
        if(percentage == null) {
            averagePercentage = 0;
        }
        else {
            averagePercentage=Math.round(percentage);
        }
        return new TestDetails(averagePercentage,timesTaken,income);
    }

    public UsersTest getUsersTestById(int id) {
        return usersTestRepository.findById(id);
    }

    public void createUsersTest(User user,Test test,int maxPoints,int actualPoints,int percentage) {
        UsersTest usersTest = new UsersTest();
        usersTest.setUser(user);
        usersTest.setTest(test);
        usersTest.setMaxPoints(maxPoints);
        usersTest.setActualPoints(actualPoints);
        usersTest.setPercentage(percentage);
        usersTestRepository.save(usersTest);
    }

    public boolean didUserTakeTest(User user,Test test) {
        return usersTestRepository.existsByUserAndTest(user,test);
    }
}
