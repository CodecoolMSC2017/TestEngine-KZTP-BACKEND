package com.kztp.testengine.service;

import com.kztp.testengine.exception.InvalidVoteException;
import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.PoolPoint;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import com.kztp.testengine.model.enums.VoteEnum;
import com.kztp.testengine.repository.PoolPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class PoolPointService {

    @Autowired
    private PoolPointRepository poolPointRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;


    public void vote(String vote,int testId) throws InvalidVoteException, UnauthorizedRequestException {
        Test test = testService.getTestById(testId);
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user.getRank().equals("newbie")) {
            throw new UnauthorizedRequestException("Your rank is too low to vote on pool tests.");
        }
        if(test.isLive()) {
            throw new InvalidVoteException("This test is already live,you can't vote on it.");
        }
        if(vote.equals("positive") || vote.equals("negative")) {
            throw new IllegalArgumentException("Only positive and negative votes are accepted");
        }
        if(poolPointRepository.existsByVoterAndTest(user,test)) {
            PoolPoint poolPoint = poolPointRepository.getByTestAndVoter(test,user);
            if(vote.equals("positive") && poolPoint.getVote() == VoteEnum.VOTE_POSITIVE.getValue()) {
                poolPointRepository.deleteById(poolPoint.getId());
            }
            else if(vote.equals("positive") && poolPoint.getVote() == VoteEnum.VOTE_NEGATIVE.getValue()) {
                poolPoint.setVote(VoteEnum.VOTE_POSITIVE.getValue());
                poolPointRepository.save(poolPoint);
            }
            else if(vote.equals("negative") && poolPoint.getVote() == VoteEnum.VOTE_POSITIVE.getValue()) {
                poolPoint.setVote(VoteEnum.VOTE_NEGATIVE.getValue());
                poolPointRepository.save(poolPoint);
            }
            else if(vote.equals("negative") && poolPoint.getVote() == VoteEnum.VOTE_NEGATIVE.getValue()) {
                poolPointRepository.deleteById(poolPoint.getId());
            }
        }
        else {
            PoolPoint poolPoint = new PoolPoint();
            poolPoint.setTest(test);
            poolPoint.setVoter(user);
            if(vote.equals("positive")) {
                poolPoint.setVote(VoteEnum.VOTE_POSITIVE.getValue());
                poolPointRepository.save(poolPoint);
            }
            else{
                poolPoint.setVote(VoteEnum.VOTE_NEGATIVE.getValue());
                poolPointRepository.save(poolPoint);
            }
        }
        checkPoolPointsLive(test);

    }

    private void checkPoolPointsLive(Test test) {
        int positive =poolPointRepository.getAllByTestAndVote(test,VoteEnum.VOTE_POSITIVE.getValue()).size();
        int negative = poolPointRepository.getAllByTestAndVote(test,VoteEnum.VOTE_NEGATIVE.getValue()).size();
        if(positive-negative >= 50) {
            testService.setLive(test);
        }
    }

}
