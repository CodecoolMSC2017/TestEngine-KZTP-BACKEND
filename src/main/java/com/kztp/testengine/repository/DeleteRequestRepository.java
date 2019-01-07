package com.kztp.testengine.repository;

import com.kztp.testengine.model.DeleteRequest;
import com.kztp.testengine.model.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeleteRequestRepository extends JpaRepository<DeleteRequest,Integer>{

    DeleteRequest findById(int id);
    Page<DeleteRequest> findBySolvedFalse(Pageable pageable);
    DeleteRequest findByTest(Test test);

}
