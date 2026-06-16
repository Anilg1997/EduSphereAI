package com.example.graphql_angular.ai.tool;

import com.example.graphql_angular.model.Student;
import com.example.graphql_angular.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StudentTools {

    private final StudentService studentService;

    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    public Student getStudent(String id) {
        return studentService.getById(id);
    }

    public Student createStudent(String name, String email, String course) {
        Student s = new Student();
        s.setName(name);
        s.setEmail(email);
        s.setCourse(course);
        return studentService.create(s);
    }

    public Student updateStudent(String id, String name, String email, String course) {
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setEmail(email);
        s.setCourse(course);
        return studentService.update(s);
    }

    public String deleteStudent(String id) {
        return studentService.delete(id);
    }

    public String getToolsDescription() {
        return """
                Available tools:
                1. getAllStudents() - Get all students
                2. getStudent(id) - Get student by ID
                3. createStudent(name, email, course) - Create a new student
                4. updateStudent(id, name, email, course) - Update an existing student
                5. deleteStudent(id) - Delete a student by ID
                """;
    }
}
