package com.hcltech.service;

import com.hcltech.dto.PaymentDTO;
import com.hcltech.entity.Invoice;
import com.hcltech.entity.Payment;
import com.hcltech.repository.InvoiceRepository;
import com.hcltech.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<PaymentDTO> getInvoicePayments(Long invoiceId) throws Exception {
        invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new Exception("Invoice not found"));

        return paymentRepository.findByInvoiceInvoiceId(invoiceId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Payment recordPayment(Long invoiceId, Payment paymentData) throws Exception {
        Invoice invoice = invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new Exception("Invoice not found"));

        paymentData.setInvoice(invoice);
        paymentData.setStatus(Payment.PaymentStatus.SUCCESS);

        Payment savedPayment = paymentRepository.save(paymentData);

        // Update invoice status if fully paid
        if (invoice.getTotalAmount().compareTo(paymentData.getAmount()) == 0) {
            invoice.setStatus(Invoice.InvoiceStatus.PAID);
            invoiceRepository.save(invoice);
        }

        return savedPayment;
    }

    public Payment getPaymentById(Long paymentId) throws Exception {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new Exception("Payment not found"));
    }

    private PaymentDTO convertToDTO(Payment payment) {
        return new PaymentDTO(
            payment.getPaymentId(),
            payment.getInvoice().getInvoiceId(),
            payment.getPaymentDate(),
            payment.getAmount(),
            payment.getPaymentMethod(),
            payment.getStatus().toString()
        );
    }
}
