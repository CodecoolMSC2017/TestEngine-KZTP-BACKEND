package com.kztp.testengine.service;

import com.kztp.testengine.exception.InvalidUploadTypeException;
import com.kztp.testengine.model.Question;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public Test createTest(String title,String description, int price, int maxPoints, List<Question> questions) {
        String fileName = xmlService.createXml(maxPoints,questions);
        Test test = new Test();
        test.setTitle(title);
        test.setDescriptiom(description);
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
        test.setDescriptiom(description);
        test.setPath(fileName);
        test.setPrice(price);
        test.setMaxPoints(maxpoints);
        test.setCreator(userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        addTestToDatabase(test);
        return test;
    }
}
