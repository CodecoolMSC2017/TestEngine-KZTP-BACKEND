package com.kztp.testengine.service;

import com.kztp.testengine.model.Question;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private XMLService xmlService;

    public Test getTestById(int id){
        return testRepository.findById(id);
    }

    public Page<Test> findAll(Pageable pageable) {
        return testRepository.findAll(pageable);
    }

    private Test addTestToDatabase(Test test){
        return testRepository.save(test);
    }

    public Test createTest(String fileName, int price, int maxPoints, List<Question> questions) {
        xmlService.createXml(maxPoints,fileName,questions);
        Test test = new Test();
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxPoints);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }
}
