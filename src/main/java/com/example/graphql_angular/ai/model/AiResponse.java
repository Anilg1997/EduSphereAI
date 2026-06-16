package com.example.graphql_angular.ai.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class AiResponse {
    private String response;
    private List<String> sources;
    private Map<String, Object> metadata;
}
