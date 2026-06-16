package com.example.graphql_angular.repository;

import com.example.graphql_angular.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository
        extends MongoRepository<Student, String> {
}