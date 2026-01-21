package com.hcltech.controller;

import com.hcltech.dto.ApiResponse;
import com.hcltech.dto.UsageRecordDTO;
import com.hcltech.entity.UsageRecord;
import com.hcltech.service.UsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services/{serviceId}/usage")
@CrossOrigin(origins = "*")
public class UsageController {

    @Autowired
    private UsageService usageService;

    @GetMapping
    public ResponseEntity<?> getServiceUsage(@PathVariable Long serviceId) {
        try {
            List<UsageRecordDTO> records = usageService.getServiceUsage(serviceId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> recordUsage(@PathVariable Long serviceId, @RequestBody UsageRecord usageRecord) {
        try {
            UsageRecord created = usageService.recordUsage(serviceId, usageRecord);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Usage recorded successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
