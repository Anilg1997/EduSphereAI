package com.example.graphql_angular.controller;


import com.example.graphql_angular.model.Student;
import com.example.graphql_angular.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StudentResolver {

    private final StudentService service;

    @QueryMapping
    public List<Student> getStudents() {
        return service.getAll();
    }

    @QueryMapping
    public Student getStudent(@Argument String id) {
        return service.getById(id);
    }

    @MutationMapping
    public Student createStudent(
            @Argument String name,
            @Argument String email,
            @Argument String course) {

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);

        return service.create(student);
    }

    @MutationMapping
    public Student updateStudent(
            @Argument String id,
            @Argument String name,
            @Argument String email,
            @Argument String course) {

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);

        return service.update(student);
    }

    @MutationMapping
    public String deleteStudent(@Argument String id) {
        return service.delete(id);
    }
}