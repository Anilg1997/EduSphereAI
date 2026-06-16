package com.example.graphql_angular.ai.repository;

import com.example.graphql_angular.ai.model.DocumentRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DocumentRecord, String> {
}
