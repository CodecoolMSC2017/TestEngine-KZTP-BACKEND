package com.kztp.testengine.service;

import com.kztp.testengine.model.*;
import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    private Test addTestToDatabase(Test test){
        return testRepository.save(test);
    }

    public Test createTest(String title,String description, int price, int maxPoints, List<Question> questions) {
        String fileName = xmlService.createXml(maxPoints,questions);
        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxPoints);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }

    public Test createTestFromUploadedXml(String fileName,String title, String description, int price, int maxpoints)  {
        Test test = new Test();
        test.setTitle(title);
        test.setDescription(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxpoints);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }

    //User user, Test test,int maxPoints,int actualPoints,int percentage

    public int takeTest(UserSolution userSolution) {
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
        return percentage;
    }
}
