package com.example.graphql_angular.ai.service;

import com.example.graphql_angular.ai.tool.StudentTools;
import com.example.graphql_angular.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class McpService {

    private final StudentTools studentTools;

    public Map<String, Object> getCapabilities() {
        Map<String, Object> caps = new LinkedHashMap<>();
        caps.put("protocol", "model-context-protocol");
        caps.put("version", "0.1.0");
        caps.put("tools", List.of(
                Map.of("name", "getAllStudents", "description", "List all students", "parameters", List.of()),
                Map.of("name", "getStudent", "description", "Get student by ID", "parameters", List.of(
                        Map.of("name", "id", "type", "string", "required", true))),
                Map.of("name", "createStudent", "description", "Create new student", "parameters", List.of(
                        Map.of("name", "name", "type", "string", "required", true),
                        Map.of("name", "email", "type", "string", "required", true),
                        Map.of("name", "course", "type", "string", "required", true))),
                Map.of("name", "updateStudent", "description", "Update student", "parameters", List.of(
                        Map.of("name", "id", "type", "string", "required", true),
                        Map.of("name", "name", "type", "string", "required", true),
                        Map.of("name", "email", "type", "string", "required", true),
                        Map.of("name", "course", "type", "string", "required", true))),
                Map.of("name", "deleteStudent", "description", "Delete student", "parameters", List.of(
                        Map.of("name", "id", "type", "string", "required", true)))
        ));
        return caps;
    }

    public Map<String, Object> executeTool(String toolName, Map<String, Object> args) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tool", toolName);
        try {
            Object output = switch (toolName) {
                case "getAllStudents" -> studentTools.getAllStudents();
                case "getStudent" -> studentTools.getStudent((String) args.get("id"));
                case "createStudent" -> studentTools.createStudent(
                        (String) args.get("name"),
                        (String) args.get("email"),
                        (String) args.get("course"));
                case "updateStudent" -> studentTools.updateStudent(
                        (String) args.get("id"),
                        (String) args.get("name"),
                        (String) args.get("email"),
                        (String) args.get("course"));
                case "deleteStudent" -> studentTools.deleteStudent((String) args.get("id"));
                default -> throw new IllegalArgumentException("Unknown tool: " + toolName);
            };
            result.put("status", "success");
            result.put("output", output);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        return result;
    }

    public List<Map<String, Object>> processQuery(String query) {
        String lower = query.toLowerCase();
        List<Map<String, Object>> results = new ArrayList<>();

        if (lower.contains("all") || lower.contains("list") || lower.contains("students")) {
            results.add(executeTool("getAllStudents", Map.of()));
        } else if (lower.contains("create") || lower.contains("add")) {
            results.add(Map.of(
                    "status", "info",
                    "message", "To create a student, use: createStudent with name, email, course",
                    "tool", "createStudent"
            ));
        } else {
            results.add(Map.of(
                    "status", "info",
                    "message", "Available tools: " + getCapabilities().get("tools"),
                    "tool", "help"
            ));
        }
        return results;
    }
}
