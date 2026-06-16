package com.example.graphql_angular.ai.service;

import dev.langchain4j.model.ollama.OllamaChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LlmService {

    private final OllamaChatModel chatModel;

    public String chat(String message) {
        return chatModel.generate(message);
    }

    public String chatWithContext(String systemPrompt, String userMessage) {
        String prompt = systemPrompt + "\n\nUser: " + userMessage;
        return chatModel.generate(prompt);
    }
}
