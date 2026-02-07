package com.hcltech.service;

import com.hcltech.dto.CustomerProfileDTO;
import com.hcltech.dto.PaymentDTO;
import com.hcltech.dto.UsageRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatbotService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UsageService usageService;

    public CustomerProfileDTO getCustomerProfile(Long customerId) throws Exception {
        return customerService.getCustomerProfile(customerId);
    }

    public List<PaymentDTO> getPayments(Long invoiceId) throws Exception {
        return paymentService.getInvoicePayments(invoiceId);
    }

    public List<UsageRecordDTO> getUsage(Long serviceId) throws Exception {
        return usageService.getServiceUsage(serviceId);
    }
}
