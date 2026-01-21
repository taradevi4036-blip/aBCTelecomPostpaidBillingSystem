package com.hcltech.service;

import com.hcltech.dto.ServiceDTO;
import com.hcltech.entity.Customer;
import com.hcltech.repository.CustomerRepository;
import com.hcltech.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<ServiceDTO> getCustomerServices(Long customerId) throws Exception {
        customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        return serviceRepository.findByCustomerCustomerId(customerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public com.hcltech.entity.Service addService(Long customerId, com.hcltech.entity.Service serviceData) throws Exception {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new Exception("Customer not found"));

        serviceData.setCustomer(customer);
        return serviceRepository.save(serviceData);
    }

    public com.hcltech.entity.Service getServiceById(Long serviceId) throws Exception {
        return serviceRepository.findById(serviceId)
            .orElseThrow(() -> new Exception("Service not found"));
    }

    public com.hcltech.entity.Service updateService(Long serviceId, com.hcltech.entity.Service updatedService) throws Exception {
        com.hcltech.entity.Service service = getServiceById(serviceId);

        if (updatedService.getServiceType() != null) {
            service.setServiceType(updatedService.getServiceType());
        }
        if (updatedService.getStatus() != null) {
            service.setStatus(updatedService.getStatus());
        }

        return serviceRepository.save(service);
    }

    private ServiceDTO convertToDTO(com.hcltech.entity.Service service) {
        return new ServiceDTO(
            service.getServiceId(),
            service.getCustomer().getCustomerId(),
            service.getServiceType(),
            service.getStartDate(),
            service.getStatus().toString()
        );
    }
}
