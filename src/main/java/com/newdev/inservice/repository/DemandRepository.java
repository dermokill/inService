package com.newdev.inservice.repository;


import com.newdev.inservice.models.Demand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandRepository extends MongoRepository<Demand,String> {


    Page<Demand> findByTaskerId(String id, Pageable pageable);
}
