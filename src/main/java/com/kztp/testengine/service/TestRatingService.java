package com.kztp.testengine.service;

import com.kztp.testengine.exception.InvalidVoteException;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.TestRating;
import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.TestRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestRatingService {

    @Autowired
    private TestRatingRepository testRatingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @Autowired
    private UsersTestService usersTestService;

    public void rate(int vote,int testId) throws InvalidVoteException {
        Test test = testService.getTestById(testId);
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(!test.isLive()) {
            throw new InvalidVoteException("This isn't live yet,you can't rate it.");
        }
        if(vote > 5 || vote < 1) {
            throw new InvalidVoteException("Rating is allowed only between 1 and 5.");
        }

        if(testRatingRepository.existsByTestAndVoter(test,user)) {
            TestRating testRating = testRatingRepository.findByTestAndVoter(test,user);
            testRating.setVote(vote);
            testRatingRepository.save(testRating);
        }
        else {
            TestRating testRating = new TestRating();
            testRating.setTest(test);
            testRating.setVoter(user);
            testRating.setVote(vote);
            testRatingRepository.save(testRating);
        }
        countRatings(test);


    }

    private void countRatings(Test test) {
        List<TestRating> testRatings =testRatingRepository.findAllByTest(test);
        float ratingSum =0;
        float rating = 0;
        for(TestRating testRating : testRatings) {
            ratingSum += testRating.getVote();
        }
        rating = ratingSum/testRatings.size();
        testService.setRating(test,rating);
    }

    public boolean didUserRateTest(int testId) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Test test = testService.getTestById(testId);
        return testRatingRepository.existsByTestAndVoter(test,user);
    }
}
