package com.example.graphql_angular.ai.service;

import com.example.graphql_angular.ai.model.AiResponse;
import com.example.graphql_angular.ai.model.DocumentRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RagService {

    private final VectorStoreService vectorStoreService;
    private final LlmService llmService;

    public AiResponse ask(String question) {
        List<DocumentRecord> relevantDocs = vectorStoreService.search(question, 3);

        String context = relevantDocs.stream()
                .map(d -> "Title: " + d.getTitle() + "\nContent: " + d.getContent())
                .collect(Collectors.joining("\n\n---\n\n"));

        List<String> sources = relevantDocs.stream()
                .map(DocumentRecord::getTitle)
                .toList();

        String systemPrompt = "You are a helpful AI assistant. Answer the question based on the provided context. "
                + "If the context doesn't contain enough information, say so honestly."
                + "\n\nContext:\n" + (context.isEmpty() ? "No relevant context found." : context);

        String response = llmService.chatWithContext(systemPrompt, question);

        return new AiResponse(
                response,
                sources,
                Map.of("context_chunks", relevantDocs.size(), "model", "llama3.2")
        );
    }
}
