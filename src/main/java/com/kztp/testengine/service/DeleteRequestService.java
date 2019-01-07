package com.kztp.testengine.service;

import com.kztp.testengine.exception.UnauthorizedRequestException;
import com.kztp.testengine.model.DeleteRequest;
import com.kztp.testengine.model.Test;
import com.kztp.testengine.model.User;
import com.kztp.testengine.repository.DeleteRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class DeleteRequestService {

    @Autowired
    private DeleteRequestRepository deleteRequestRepository;

    @Autowired
    private UserService userService;

    public Page<DeleteRequest> getAllUnSolvedDeleteRequest(Pageable pageable) throws UnauthorizedRequestException {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(!user.getAuthorities().contains("ROLE_ADMIN")) {
            throw new UnauthorizedRequestException("You aren't permitted for this action.");
        }
        return deleteRequestRepository.findBySolvedFalse(pageable);
    }

    public void createDeleteRequest(Test test) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setTest(test);
        deleteRequestRepository.save(deleteRequest);
    }

    public void setDeleteRequestSolved(int id) {
        DeleteRequest deleteRequest =deleteRequestRepository.findById(id);
        deleteRequest.setSolved(true);
        deleteRequestRepository.save(deleteRequest);
    }

    public DeleteRequest findByTest(Test test) {
        return deleteRequestRepository.findByTest(test);
    }


}
