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

    public Page<Test> findAll(Pageable pageable,boolean live,String title,int ratingMin,int ratingMax,int priceMin,int priceMax) {
        return testRepository.findAllByParameter( live, title, ratingMin, ratingMax, priceMin, priceMax,pageable);
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

    public Test createTest(String title,String description, int price, String type, List<Question> questions) throws UnauthorizedRequestException {
        User user =userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getRank().equals("newbie")){
            throw new UnauthorizedRequestException("Your rank is too low to upload tests.");
        }

        String fileName = xmlService.createXml(questions.size(),questions);
        Test test = new Test();
        if(user.getRank().equals("elite")) {
            test.setLive(true);
        }
        test.setTitle(title);
        test.setDescription(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(questions.size());
        test.setType(type);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }

    public Test createTestFromUploadedXml(String fileName,String title, String description, int price, int maxpoints, String type) throws UnauthorizedRequestException {
        User user =userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getRank().equals("newbie")){
            throw new UnauthorizedRequestException("Your rank is too low to upload tests.");
        }
        Test test = new Test();
        if(user.getRank().equals("elite")) {
            test.setLive(true);
        }
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

    public TestResult sendSolution(UserSolution userSolution) throws UserException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        int actualPoints = 0;
        Test test =getTestById(userSolution.getTestId());
        List<Question> questions=xmlService.readXml(test.getPath());
        List<Solution> solutions = userSolution.getSolutions();
        for(int i = 0;i < questions.size();i++) {
            Solution solution = new Solution();
            for (Solution solution1:solutions) {
                if(solution1.getId() == questions.get(i).getId()) {
                    solution = solution1;
                }
            }
            if (questions.get(i).getAnswer().getText().equals(solution.getSolution())) {
                actualPoints++;
            }

        }
        float percentage;
        if (actualPoints == 0) {
            percentage = 0;
        }
        else {
            percentage = (actualPoints / (float)test.getMaxPoints())*100;
        }

        if(!usersTestService.didUserTakeTest(user,test)) {
            usersTestService.createUsersTest(user,test,test.getMaxPoints(),actualPoints,Math.round(percentage));
        }
        if (!user.getRank().equals("elite")) {
            List<UsersTest> usersTest = usersTestService.getCompletedTestsByUser(user);
            checkRank(user,usersTest);
        }
        List<Solution> correctSolutions = new ArrayList<>();
        for (Question question : questions) {
            Solution solution = new Solution(question.getId(),question.getAnswer().getText());
            correctSolutions.add(solution);
        }
        return new TestResult(Math.round(percentage),correctSolutions);
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

    public boolean isTestTaken(int testId) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Test test = testRepository.findById(testId);
        return usersTestService.didUserTakeTest(user,test);
    }
}
