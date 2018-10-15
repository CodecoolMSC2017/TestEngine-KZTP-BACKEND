package com.kztp.testengine.controller;

import com.kztp.testengine.exception.InvalidUploadTypeException;
import com.kztp.testengine.model.Question;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.UserSolution;
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
@RequestMapping("/test")
public class TestController {
    private final int PAGESIZE = 10;
    @Autowired
    private TestService testService;

    @Autowired
    private XMLService xmlService;

    @GetMapping("/{id}")
    public Test getTestById(@PathVariable("id")int id){
        return testService.getTestById(id);
    }
    @GetMapping("/all")
    public Page<Test> getAllTest(@RequestParam("page") int pageNumber){
        return testService.findAll(PageRequest.of(pageNumber,PAGESIZE,Sort.Direction.ASC,"type"));
    }
    @PostMapping("/newtest")
    public Test createTest(@RequestBody Map<String, Object> map) {
        String title = (String) map.get("title");
        String description = (String) map.get("description");
        int price = (Integer) map.get("price");
        int maxPoints = (Integer) map.get("maxpoints");
        List<Question> questions = (List<Question>) map.get("questions");
        return testService.createTest(title,description,price,maxPoints,questions);
    }

    @PostMapping("/xmlupload")
    public Map<String,Boolean> uploadXml(@RequestParam("file") MultipartFile file, String title, String description, int price, int maxpoints ) throws IOException, InvalidUploadTypeException {
        return xmlService.uploadXml(title,description,price,maxpoints,file);
    }

    @PostMapping("/taketest")
    public int takeTest(@RequestBody UserSolution userSolution) {
        return testService.takeTest(userSolution);
    }
}
