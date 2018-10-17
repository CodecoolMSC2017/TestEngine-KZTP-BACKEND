package com.kztp.testengine.service;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.exception.UserException;
import com.kztp.testengine.model.*;
import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public final class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private XMLService xmlService;

    @Autowired
    private UsersTestService usersTestService;

    public Test getTestById(int id){
        return testRepository.findById(id);
    }

    public Page<Test> findAll(Pageable pageable) {
        return testRepository.findByLiveTrue(pageable);
    }

    public Page<Test> findAllPoolTest(Pageable pageable) throws UnauthorizedRequestException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getRank().equals("newbie")) {
            throw new UnauthorizedRequestException("Your rank is too low to see this page.");
        }
        return testRepository.findByLiveFalse(pageable);
    }

    public List<Test> findAllLiveByUserName(String userName) {
        List<Test> tests;
        if (userService.userExists(userName)) {
            User user = userService.getUserByUsername(userName);
            tests = testRepository.findByCreatorAndLiveTrue(user);
        } else {
            throw new IllegalArgumentException("No user with this name.");
        }
        return tests;
    }

    private Test addTestToDatabase(Test test){
        return testRepository.save(test);
    }

    public Test createTest(String title,String description, int price, int maxPoints, String type, List<Question> questions) throws UnauthorizedRequestException {
        if(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getRank().equals("newbie")){
            throw new UnauthorizedRequestException("Your rank is too low to upload tests.");
        }
        String fileName = xmlService.createXml(maxPoints,questions);
        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxPoints);
        test.setType(type);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }

    public Test createTestFromUploadedXml(String fileName,String title, String description, int price, int maxpoints, String type) throws UnauthorizedRequestException {
        if(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getRank().equals("newbie")){
            throw new UnauthorizedRequestException("Your rank is too low to upload tests.");
        }
        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxpoints);
        test.setType(type);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }

    public List<Question> takeTest(int testId) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Test test =testRepository.findById(testId);
        return xmlService.readXml(test.getPath());

    }

    //User user, Test test,int maxPoints,int actualPoints,int percentage

    public int sendSolution(UserSolution userSolution) throws UserException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        int actualPoints = 0;
        Test test =getTestById(userSolution.getTestId());
        List<Question> questions=xmlService.readXml(test.getPath());
        List<String> solutions = userSolution.getSolutions();
        for(int i = 0;i < questions.size();i++) {
            if(questions.get(i).getAnswer().getText().equals(solutions.get(i))) {
                actualPoints++;
            }
        }
        int percentage = test.getMaxPoints()/actualPoints;

        usersTestService.createUsersTest(user,test,test.getMaxPoints(),actualPoints,percentage);
        if (!user.getRank().equals("elite")) {
            List<UsersTest> usersTest = usersTestService.getCompletedTestsByUser(user);
            checkRank(user,usersTest);
        }
        return percentage;
    }

    private void checkRank(User user,List<UsersTest> tests) throws UserException {
        int testCount =0;
        if (user.getRank().equals("user")) { //100test 70%
            int testAbove70Count =0;
            for (UsersTest test:tests) {
                testCount++;
                if(test.getPercetage() >= 70) {
                    testAbove70Count++;
                }
            }
            if(testCount >= 100 && testAbove70Count >= 100){
                userService.rankUpUser(user);
            }
        }
        else if (user.getRank().equals("newbie")) { //15test 50%
            int testAbove50Count =0;
            for (UsersTest test:tests) {
                testCount++;
                if(test.getPercetage() >= 50) {
                    testAbove50Count++;
                }
            }
            if(testCount >= 15 && testAbove50Count >= 15) {
                userService.rankUpUser(user);
            }
        }
    }

    public void setLive(Test test) {
        test.setLive(true);
        testRepository.save(test);
    }

    public void setRating(Test test,float rating) {
        test.setRating(rating);
        testRepository.save(test);
    }

    public void setPoolRating(Test test,int poolRating) {
        test.setPoolRating(poolRating);
        testRepository.save(test);
    }
}
