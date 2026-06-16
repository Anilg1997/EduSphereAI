package com.example.graphql_angular.service;

import com.example.graphql_angular.model.Student;
import com.example.graphql_angular.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    public Student create(Student student) {
        return repository.save(student);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }

    public Student getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Student update(Student student) {
        return repository.save(student);
    }

    public String delete(String id) {
        repository.deleteById(id);
        return "Deleted Successfully";
    }
}