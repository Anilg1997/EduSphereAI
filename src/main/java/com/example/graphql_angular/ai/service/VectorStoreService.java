package com.example.graphql_angular.ai.service;

import com.example.graphql_angular.ai.model.DocumentRecord;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VectorStoreService {

    private final EmbeddingService embeddingService;

    private final Map<String, List<Float>> vectorStore = new ConcurrentHashMap<>();
    private final Map<String, DocumentRecord> documentStore = new ConcurrentHashMap<>();

    public void storeDocument(DocumentRecord doc) {
        List<Float> embedding = embeddingService.embed(doc.getContent());
        vectorStore.put(doc.getId(), embedding);
        documentStore.put(doc.getId(), doc);
    }

    public void storeDocuments(List<DocumentRecord> docs) {
        List<String> contents = docs.stream().map(DocumentRecord::getContent).toList();
        List<List<Float>> embeddings = embeddingService.embedBatch(contents);
        for (int i = 0; i < docs.size(); i++) {
            vectorStore.put(docs.get(i).getId(), embeddings.get(i));
            documentStore.put(docs.get(i).getId(), docs.get(i));
        }
    }

    public List<DocumentRecord> search(String query, int topK) {
        List<Float> queryEmbedding = embeddingService.embed(query);
        return vectorStore.entrySet().stream()
                .map(e -> {
                    double score = cosineSimilarity(queryEmbedding, e.getValue());
                    return new AbstractMap.SimpleEntry<>(e.getKey(), score);
                })
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topK)
                .filter(e -> e.getValue() > 0.5)
                .map(e -> documentStore.get(e.getKey()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<DocumentRecord> getAllDocuments() {
        return new ArrayList<>(documentStore.values());
    }

    public void deleteDocument(String id) {
        vectorStore.remove(id);
        documentStore.remove(id);
    }

    public void clearAll() {
        vectorStore.clear();
        documentStore.clear();
    }

    private double cosineSimilarity(List<Float> a, List<Float> b) {
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
