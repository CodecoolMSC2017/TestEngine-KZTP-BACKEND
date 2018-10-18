package com.kztp.testengine.controller;

import com.kztp.testengine.exception.InvalidUploadTypeException;
import com.kztp.testengine.exception.InvalidVoteException;
import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.exception.UserException;
import com.kztp.testengine.model.*;
import com.kztp.testengine.service.PoolPointService;
import com.kztp.testengine.service.TestRatingService;
import com.kztp.testengine.service.TestService;
import com.kztp.testengine.service.XMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    private final int PAGESIZE = 10;
    @Autowired
    private TestService testService;

    @Autowired
    private XMLService xmlService;

    @Autowired
    private TestRatingService testRatingService;

    @Autowired
    private PoolPointService poolPointService;

    @GetMapping("/test/{id}")
    public Test getTestById(@PathVariable("id")int id){
        return testService.getTestById(id);
    }
    @GetMapping("/test/all")
    public Page<Test> getAllTest(@RequestParam("page") int pageNumber){
        return testService.findAll(PageRequest.of(pageNumber,PAGESIZE,Sort.Direction.ASC,"type"));
    }
    @GetMapping("/user/test/pool")
    public Page<Test> getPoolTests(@RequestParam("page") int pageNumber) throws UnauthorizedRequestException {
        return testService.findAllPoolTest(PageRequest.of(pageNumber,PAGESIZE,Sort.Direction.ASC,"type"));
    }
    @PostMapping("/user/test/newtest")
    public Test createTest(@RequestBody NewTest newTest) throws UnauthorizedRequestException {
        String title = newTest.getTitle();
        String description = newTest.getDescription();
        int price =newTest.getPrice();
        String type = newTest.getType();
        List<Question> questions = newTest.getQuestions();
        return testService.createTest(title,description,price,type,questions);
    }

    @PostMapping("/user/test/xmlupload")
    public Map<String,Boolean> uploadXml(@RequestParam("file") MultipartFile file, String title, String description, int price, int maxpoints, String type ) throws IOException, InvalidUploadTypeException, UnauthorizedRequestException {
        return xmlService.uploadXml(title,description,price,maxpoints,type,file);
    }

    @PostMapping("/user/test/sendsolution")
    public TestResult sendSolution(@RequestBody UserSolution userSolution) throws UserException {
        return testService.sendSolution(userSolution);
    }

    @GetMapping("/user/tests/{username}")
    public List<Test> getLiveTestsByUser(@PathVariable("username") String username) {
        return testService.findAllLiveByUserName(username);
    }

    @GetMapping("/user/test/taketest/{id}")
    public List<Question> takeTest(@PathVariable("id") int testId) {
        return testService.takeTest(testId);
    }

    @PostMapping("/user/test/rate/{id}/{rate}")
    public void rateTest(@PathVariable("id") int testId,@PathVariable("rate") int vote) throws InvalidVoteException {
        testRatingService.rate(vote,testId);
    }

    @PostMapping("/user/pool/vote/{id}/{vote}")
    public void votePool(@PathVariable("id") int testId,@PathVariable("vote") String vote) throws InvalidVoteException, UnauthorizedRequestException {
        poolPointService.vote(vote,testId);
    }

    @GetMapping("/user/test/taken/{id}")
    public boolean isTestTaken(@PathVariable("id") int testId) {
        return testService.isTestTaken(testId);
    }
}
