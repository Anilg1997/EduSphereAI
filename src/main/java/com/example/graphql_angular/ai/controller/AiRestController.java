package com.example.graphql_angular.ai.controller;

import com.example.graphql_angular.ai.model.AiResponse;
import com.example.graphql_angular.ai.model.ChatRequest;
import com.example.graphql_angular.ai.model.DocumentRecord;
import com.example.graphql_angular.ai.repository.DocumentRepository;
import com.example.graphql_angular.ai.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiRestController {

    private final LlmService llmService;
    private final RagService ragService;
    private final VectorStoreService vectorStoreService;
    private final EmbeddingService embeddingService;
    private final AgentService agentService;
    private final McpService mcpService;
    private final DocumentRepository documentRepository;

    @PostMapping("/chat")
    public ResponseEntity<AiResponse> chat(@RequestBody ChatRequest request) {
        String response = llmService.chat(request.getMessage());
        return ResponseEntity.ok(new AiResponse(response, List.of(), Map.of("model", "llama3.2")));
    }

    @PostMapping("/rag")
    public ResponseEntity<AiResponse> ragQuery(@RequestBody ChatRequest request) {
        AiResponse response = ragService.ask(request.getMessage());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/agent")
    public ResponseEntity<AiResponse> agent(@RequestBody ChatRequest request) {
        AiResponse response = agentService.processAgentTask(request.getMessage());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/embed")
    public ResponseEntity<List<Float>> embed(@RequestBody Map<String, String> body) {
        List<Float> embedding = embeddingService.embed(body.get("text"));
        return ResponseEntity.ok(embedding);
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentRecord> addDocument(@RequestBody DocumentRecord doc) {
        DocumentRecord saved = documentRepository.save(doc);
        vectorStoreService.storeDocument(saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentRecord>> getDocuments() {
        return ResponseEntity.ok(vectorStoreService.getAllDocuments());
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentRepository.deleteById(id);
        vectorStoreService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/documents/ingest")
    public ResponseEntity<String> ingestDocuments() {
        List<DocumentRecord> docs = documentRepository.findAll();
        if (docs.isEmpty()) {
            docs.add(new DocumentRecord(null, "Introduction to Java",
                    "Java is a high-level, class-based, object-oriented programming language.",
                    "manual"));
            docs.add(new DocumentRecord(null, "Spring Boot Overview",
                    "Spring Boot makes it easy to create stand-alone Spring-based applications.",
                    "manual"));
            docs.add(new DocumentRecord(null, "GraphQL Basics",
                    "GraphQL is a query language for APIs that gives clients the power to ask for exactly what they need.",
                    "manual"));
            docs.add(new DocumentRecord(null, "MongoDB Document Database",
                    "MongoDB is a source-available, cross-platform, document-oriented database program.",
                    "manual"));
            docs.add(new DocumentRecord(null, "LangChain4j Framework",
                    "LangChain4j is a Java library that simplifies integrating LLMs into Java applications.",
                    "manual"));
            docs = documentRepository.saveAll(docs);
        }
        vectorStoreService.storeDocuments(docs);
        return ResponseEntity.ok("Ingested " + docs.size() + " documents into vector store");
    }

    @GetMapping("/mcp/capabilities")
    public ResponseEntity<Map<String, Object>> mcpCapabilities() {
        return ResponseEntity.ok(mcpService.getCapabilities());
    }

    @PostMapping("/mcp/execute")
    public ResponseEntity<Map<String, Object>> mcpExecute(@RequestBody Map<String, Object> body) {
        String tool = (String) body.get("tool");
        @SuppressWarnings("unchecked")
        Map<String, Object> args = (Map<String, Object>) body.getOrDefault("arguments", Map.of());
        return ResponseEntity.ok(mcpService.executeTool(tool, args));
    }

    @PostMapping("/mcp/query")
    public ResponseEntity<List<Map<String, Object>>> mcpQuery(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(mcpService.processQuery(body.get("query")));
    }
}
