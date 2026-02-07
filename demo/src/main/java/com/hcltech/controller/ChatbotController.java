package com.hcltech.controller;

import org.springframework.beans.factory.annotation.Autowired;
// ...existing imports...
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private com.hcltech.service.OpenAIClientService openAIClientService;

    @Autowired
    private com.hcltech.service.ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<?> askChatbot(@RequestBody String userQuery) {
        try {
            String response = openAIClientService.ask("You are a helpful assistant for ABC Telecom. Answer user questions about their services, payments, and usage. User query: " + userQuery);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/profile/{customerId}")
    public ResponseEntity<?> getCustomerProfile(@PathVariable Long customerId) {
        try {
            return ResponseEntity.ok(chatbotService.getCustomerProfile(customerId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/payments/{invoiceId}")
    public ResponseEntity<?> getPayments(@PathVariable Long invoiceId) {
        try {
            return ResponseEntity.ok(chatbotService.getPayments(invoiceId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usage/{serviceId}")
    public ResponseEntity<?> getUsage(@PathVariable Long serviceId) {
        try {
            return ResponseEntity.ok(chatbotService.getUsage(serviceId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
