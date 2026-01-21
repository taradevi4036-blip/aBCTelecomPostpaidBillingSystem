package com.hcltech.service;

import com.hcltech.dto.CustomerProfileDTO;
import com.hcltech.entity.Customer;
import com.hcltech.entity.User;
import com.hcltech.repository.CustomerRepository;
import com.hcltech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public CustomerProfileDTO getCustomerProfile(Long customerId) throws Exception {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        CustomerProfileDTO dto = new CustomerProfileDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setUserId(customer.getUser().getUserId());
        dto.setFullName(customer.getFullName());
        dto.setAddress(customer.getAddress());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setUsername(customer.getUser().getUsername());
        dto.setEmail(customer.getUser().getEmail());

        return dto;
    }

    public Customer createCustomer(Long userId, Customer customer) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));

        customer.setUser(user);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, Customer updatedCustomer) throws Exception {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        if (updatedCustomer.getFullName() != null) {
            customer.setFullName(updatedCustomer.getFullName());
        }
        if (updatedCustomer.getAddress() != null) {
            customer.setAddress(updatedCustomer.getAddress());
        }
        if (updatedCustomer.getPhoneNumber() != null) {
            customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        }

        return customerRepository.save(customer);
    }

    public Customer getCustomerByUserId(Long userId) throws Exception {
        return customerRepository.findByUserUserId(userId)
            .orElseThrow(() -> new Exception("Customer profile not found for this user"));
    }
}
