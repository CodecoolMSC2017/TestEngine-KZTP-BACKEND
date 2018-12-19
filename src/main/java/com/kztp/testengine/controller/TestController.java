package com.kztp.testengine.controller;

import com.kztp.testengine.exception.InvalidUploadTypeException;
import com.kztp.testengine.exception.InvalidVoteException;
import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.exception.UserException;
import com.kztp.testengine.model.*;
import com.kztp.testengine.service.*;
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

    @Autowired
    private UsersTestService usersTestService;

    @Autowired
    private TestReportService testReportService;

    @Autowired
    private UserService userService;

    @GetMapping("/test/{id}")
    public Test getTestById(@PathVariable("id")int id){
        return testService.getTestById(id);
    }
    @GetMapping("/test/all")
    public Page<Test> getAllTest(@RequestParam("page") int pageNumber,
                                 @RequestParam("pagesize") int pageSize,
                                 @RequestParam("order") String order,
                                 @RequestParam("orderby") String orderby,
                                 @RequestParam("live") boolean live,
                                 @RequestParam("title") String title,
                                 @RequestParam("ratingMin") int ratingMin,
                                 @RequestParam("ratingMax") int ratingMax,
                                 @RequestParam("priceMin") int priceMin,
                                 @RequestParam("priceMax") int priceMax) throws UnauthorizedRequestException {
        if(title.equals("")) {
            title = "%";
        }
        else {
            title = "%" + title + "%";
        }
        if(orderby.equals("")) {
            orderby = "type";
        }
        if(order.equals("none") || order.equals("asc")) {
            return testService.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, orderby), live, title, ratingMin, ratingMax, priceMin, priceMax);
        }
        else {
            return testService.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, orderby), live, title, ratingMin, ratingMax, priceMin, priceMax);
        }
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
    public float rateTest(@PathVariable("id") int testId,@PathVariable("rate") int vote) throws InvalidVoteException {
        return testRatingService.rate(vote,testId);
    }

    @PostMapping("/user/test/vote/{id}/{vote}")
    public int votePool(@PathVariable("id") int testId,@PathVariable("vote") String vote) throws InvalidVoteException, UnauthorizedRequestException {
        return poolPointService.vote(vote,testId);
    }

    @GetMapping("/user/test/taken/{id}")
    public boolean isTestTaken(@PathVariable("id") int testId) {
        return testService.isTestTaken(testId);
    }

    @GetMapping("/user/test/voted/{id}")
    public boolean didUserVotePool(@PathVariable("id") int testId) {
        return testRatingService.didUserRateTest(testId);
    }

    @GetMapping("/user/test/rated/{id}")
    public boolean didUserRateTest(@PathVariable("id") int testId) {
        return poolPointService.didUserVotePool(testId);
    }

    @GetMapping("/user/teststaken")
    public List<UsersTest> getTakenTestsForLoggedUser(){
       return usersTestService.getLoggedUserCompletedTests();
    }

    @GetMapping("/admin/test/reportedtests")
    public Page<TestReport> getReportedTestsForUser(@RequestParam("page") int pageNumber,
                                                    @RequestParam("pagesize") int pageSize){
        return testReportService.getAllReportedTest(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"));
    }

    @GetMapping("/admin/test/reportedtests/{id}")
    public Page<TestReport> getReportedTestForUser(@PathVariable("id") int id, @RequestParam("page") int pageNumber,
                                                   @RequestParam("pagesize") int pageSize){
        return testReportService.getAllReportedTestByUser(userService.getUserById(id),PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"));
    }

    @GetMapping("/user/test/myreports")
    public Page<TestReport> getLoggedUserReportedTest(@RequestParam("page") int pageNumber, @RequestParam("pagesize") int pageSize){
        return testReportService.getAllReportedTestByForLoggedUser(PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"));
    }

    @GetMapping("/user/test/reports/{id}")
    public Page<TestReport> getReportsForTest(@PathVariable("id") int id, @RequestParam("page") int pageNumber,
                                              @RequestParam("pagesize") int pageSize){
        return testReportService.getAllReportedTestByTest(testService.getTestById(id),PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, "id"));
    }

    @PostMapping("/user/test/report/{id}")
    public String reportTest(@PathVariable("id") int testId,@RequestBody String description) {
        return testReportService.reportTest(testService.getTestById(testId),description);
    }

    @PostMapping("/admin/report/solved/{id}")
    public void resolveReport(@PathVariable("id") int reportId) {
        testReportService.resolveReport(reportId);
    }
}
