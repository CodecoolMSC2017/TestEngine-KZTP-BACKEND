package com.kztp.testengine.service;

import com.kztp.testengine.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TestService {
    @Autowired
    private TestRepository testRepository;
}
