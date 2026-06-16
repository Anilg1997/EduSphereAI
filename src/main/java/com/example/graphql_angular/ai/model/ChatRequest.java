package com.example.graphql_angular.ai.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatRequest {
    private String message;
    private String sessionId;
    private List<Map<String, String>> history;
}
