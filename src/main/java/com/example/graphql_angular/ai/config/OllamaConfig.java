package com.example.graphql_angular.ai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ollama")
public class OllamaConfig {
    private String baseUrl = "http://localhost:11434";
    private String chatModel = "llama3.2";
    private String embeddingModel = "nomic-embed-text";
    private double temperature = 0.7;
    private int maxTokens = 2000;
}
