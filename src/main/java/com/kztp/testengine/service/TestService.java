package com.kztp.testengine.service;

import com.kztp.testengine.model.Test;
import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public final class TestService {
    @Autowired
    private TestRepository testRepository;

    public Test getTestById(int id){
        return testRepository.findById(id);
    }

    public Page<Test> findAll(Pageable pageable) {
        return testRepository.findAll(pageable);
    }

    private Test addTestToDatabase(Test test){
        return testRepository.save(test);
    }
}
