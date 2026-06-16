package com.example.graphql_angular.ai.controller;

import com.example.graphql_angular.ai.model.AiResponse;
import com.example.graphql_angular.ai.model.DocumentRecord;
import com.example.graphql_angular.ai.repository.DocumentRepository;
import com.example.graphql_angular.ai.service.*;
import com.example.graphql_angular.ai.tool.StudentTools;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AiGraphQLResolver {

    private final LlmService llmService;
    private final RagService ragService;
    private final AgentService agentService;
    private final McpService mcpService;
    private final VectorStoreService vectorStoreService;
    private final DocumentRepository documentRepository;

    @QueryMapping
    public AiResponse aiChat(@Argument String message) {
        String response = llmService.chat(message);
        return new AiResponse(response, List.of(), Map.of("model", "llama3.2"));
    }

    @QueryMapping
    public AiResponse aiRag(@Argument String question) {
        return ragService.ask(question);
    }

    @QueryMapping
    public AiResponse aiAgent(@Argument String task) {
        return agentService.processAgentTask(task);
    }

    @QueryMapping
    public List<DocumentRecord> getDocuments() {
        return vectorStoreService.getAllDocuments();
    }

    @QueryMapping
    public List<Map<String, Object>> mcpCapabilities() {
        Object tools = mcpService.getCapabilities().get("tools");
        if (tools instanceof List) {
            return (List<Map<String, Object>>) tools;
        }
        return List.of();
    }

    @MutationMapping
    public DocumentRecord addDocument(
            @Argument String title,
            @Argument String content,
            @Argument String source) {
        DocumentRecord doc = new DocumentRecord(null, title, content,
                source != null ? source : "graphql");
        DocumentRecord saved = documentRepository.save(doc);
        vectorStoreService.storeDocument(saved);
        return saved;
    }

    @MutationMapping
    public String ingestDocuments() {
        List<DocumentRecord> docs = documentRepository.findAll();
        if (docs.isEmpty()) {
            docs.add(new DocumentRecord(null, "Introduction to Java",
                    "Java is a high-level, class-based, object-oriented programming language.", "seed"));
            docs.add(new DocumentRecord(null, "Spring Boot Overview",
                    "Spring Boot makes it easy to create stand-alone Spring-based applications.", "seed"));
            docs.add(new DocumentRecord(null, "GraphQL Basics",
                    "GraphQL is a query language for APIs.", "seed"));
            docs.add(new DocumentRecord(null, "MongoDB Document Database",
                    "MongoDB is a document-oriented database program.", "seed"));
            docs.add(new DocumentRecord(null, "AI and Machine Learning",
                    "AI is the simulation of human intelligence in machines.", "seed"));
            docs = documentRepository.saveAll(docs);
        }
        vectorStoreService.storeDocuments(docs);
        return "Ingested " + docs.size() + " documents";
    }

    @MutationMapping
    public String deleteDocument(@Argument String id) {
        documentRepository.deleteById(id);
        vectorStoreService.deleteDocument(id);
        return "Deleted";
    }

    @MutationMapping
    public Map<String, Object> mcpExecute(
            @Argument String tool,
            @Argument Map<String, Object> arguments) {
        return mcpService.executeTool(tool, arguments != null ? arguments : Map.of());
    }
}
