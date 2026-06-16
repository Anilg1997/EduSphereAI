package com.example.graphql_angular.ai.controller;

import com.example.graphql_angular.ai.service.McpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/mcp")
@RequiredArgsConstructor
public class McpController {

    private final McpService mcpService;
    private final ConcurrentHashMap<String, Object> sessions = new ConcurrentHashMap<>();

    @GetMapping("/capabilities")
    public ResponseEntity<Map<String, Object>> capabilities() {
        return ResponseEntity.ok(mcpService.getCapabilities());
    }

    @PostMapping(value = "/rpc", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> rpc(@RequestBody Map<String, Object> body) {
        String method = (String) body.getOrDefault("method", "");
        Map<String, Object> params = body.containsKey("params")
                ? (Map<String, Object>) body.get("params")
                : Map.of();

        return switch (method) {
            case "initialize" -> {
                String sessionId = (String) params.getOrDefault("sessionId", "default");
                sessions.put(sessionId, true);
                yield ResponseEntity.ok(Map.of(
                        "jsonrpc", "2.0",
                        "result", Map.of("sessionId", sessionId, "capabilities", mcpService.getCapabilities())
                ));
            }
            case "tools/list" ->
                ResponseEntity.ok(Map.of("jsonrpc", "2.0", "result", mcpService.getCapabilities()));
            case "tools/call" -> {
                String toolName = (String) params.get("name");
                Map<String, Object> args = params.containsKey("arguments")
                        ? (Map<String, Object>) params.get("arguments")
                        : Map.of();
                Map<String, Object> result = mcpService.executeTool(toolName, args);
                yield ResponseEntity.ok(Map.of("jsonrpc", "2.0", "result", result));
            }
            default -> ResponseEntity.ok(Map.of(
                    "jsonrpc", "2.0",
                    "error", Map.of("code", -32601, "message", "Method not found: " + method)
            ));
        };
    }
}
