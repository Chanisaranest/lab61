package com.example.lab61.repository;

import com.example.lab61.pojo.Wizard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends MongoRepository<Wizard, String> {
    @Query(value = "{_id: '?0'}")
    public Wizard findByID(String _id);
}
