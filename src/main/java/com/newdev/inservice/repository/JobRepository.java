package com.newdev.inservice.repository;

import com.newdev.inservice.models.Demand;
import com.newdev.inservice.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {


    Page<Job> findByTaskerId(String id, Pageable pageable);

    Page<Job> findByClientId(String id, Pageable pageable);
}
