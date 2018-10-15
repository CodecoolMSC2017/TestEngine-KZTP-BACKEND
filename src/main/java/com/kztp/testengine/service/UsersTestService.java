package com.kztp.testengine.service;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import com.kztp.testengine.model.UsersTest;
import com.kztp.testengine.repository.UsersTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class UsersTestService {

    @Autowired
    private UsersTestRepository usersTestRepository;

    public List<UsersTest> getCompletedTestsByUser(User user) {
        return usersTestRepository.findByUser(user);
    }

    public List<UsersTest> getCompletedTestsByTest(Test test) {
        return usersTestRepository.findByTest(test);
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
        usersTest.setPercetage(percentage);
        usersTestRepository.save(usersTest);
    }
}
