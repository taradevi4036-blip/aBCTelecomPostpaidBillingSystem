package com.hcltech.controller;

import com.hcltech.dto.ApiResponse;
import com.hcltech.dto.PaymentDTO;
import com.hcltech.entity.Payment;
import com.hcltech.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices/{invoiceId}/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<?> getInvoicePayments(@PathVariable Long invoiceId) {
        try {
            List<PaymentDTO> payments = paymentService.getInvoicePayments(invoiceId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> recordPayment(@PathVariable Long invoiceId, @RequestBody Payment payment) {
        try {
            Payment created = paymentService.recordPayment(invoiceId, payment);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Payment recorded successfully", created));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
