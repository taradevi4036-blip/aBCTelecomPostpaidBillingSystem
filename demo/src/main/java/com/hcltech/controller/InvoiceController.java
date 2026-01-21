package com.hcltech.controller;

import com.hcltech.dto.ApiResponse;
import com.hcltech.dto.InvoiceDTO;
import com.hcltech.entity.Invoice;
import com.hcltech.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers/{customerId}/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<?> getCustomerInvoices(@PathVariable Long customerId) {
        try {
            List<InvoiceDTO> invoices = invoiceService.getCustomerInvoices(customerId);
            return ResponseEntity.ok(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> generateInvoice(@PathVariable Long customerId, @RequestBody Invoice invoice) {
        try {
            Invoice created = invoiceService.generateInvoice(customerId, invoice);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Invoice generated successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
