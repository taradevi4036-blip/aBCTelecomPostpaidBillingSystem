package com.hcltech.service;

import com.hcltech.dto.InvoiceDTO;
import com.hcltech.entity.Customer;
import com.hcltech.entity.Invoice;
import com.hcltech.repository.CustomerRepository;
import com.hcltech.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<InvoiceDTO> getCustomerInvoices(Long customerId) throws Exception {
        customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        return invoiceRepository.findByCustomerCustomerId(customerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Invoice generateInvoice(Long customerId, Invoice invoiceData) throws Exception {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        invoiceData.setCustomer(customer);
        invoiceData.setStatus(Invoice.InvoiceStatus.UNPAID);
        return invoiceRepository.save(invoiceData);
    }

    public Invoice getInvoiceById(Long invoiceId) throws Exception {
        return invoiceRepository.findById(invoiceId)
            .orElseThrow(() -> new Exception("Invoice not found"));
    }

    public Invoice updateInvoice(Long invoiceId, Invoice updatedInvoice) throws Exception {
        Invoice invoice = getInvoiceById(invoiceId);

        if (updatedInvoice.getStatus() != null) {
            invoice.setStatus(updatedInvoice.getStatus());
        }
        if (updatedInvoice.getTotalAmount() != null) {
            invoice.setTotalAmount(updatedInvoice.getTotalAmount());
        }

        return invoiceRepository.save(invoice);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        return new InvoiceDTO(
            invoice.getInvoiceId(),
            invoice.getCustomer().getCustomerId(),
            invoice.getBillingPeriodStart(),
            invoice.getBillingPeriodEnd(),
            invoice.getTotalAmount(),
            invoice.getStatus().toString()
        );
    }
}
