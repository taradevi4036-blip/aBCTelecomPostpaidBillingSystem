package com.hcltech.controller;

import com.hcltech.dto.ApiResponse;
import com.hcltech.dto.ServiceDTO;
import com.hcltech.entity.Service;
import com.hcltech.service.ServiceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    @Autowired
    private ServiceManagementService serviceManagementService;

    @GetMapping
    public ResponseEntity<?> getCustomerServices(@PathVariable Long customerId) {
        try {
            List<ServiceDTO> services = serviceManagementService.getCustomerServices(customerId);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addService(@PathVariable Long customerId, @RequestBody Service service) {
        try {
            Service created = serviceManagementService.addService(customerId, service);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Service created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
