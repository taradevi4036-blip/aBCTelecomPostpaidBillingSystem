package com.hcltech.service;

import com.hcltech.dto.UsageRecordDTO;
import com.hcltech.entity.UsageRecord;
import com.hcltech.repository.ServiceRepository;
import com.hcltech.repository.UsageRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsageService {

    @Autowired
    private UsageRecordRepository usageRecordRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<UsageRecordDTO> getServiceUsage(Long serviceId) throws Exception {
        serviceRepository.findById(serviceId)
            .orElseThrow(() -> new Exception("Service not found"));

        return usageRecordRepository.findByServiceServiceId(serviceId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public UsageRecord recordUsage(Long serviceId, UsageRecord usageRecord) throws Exception {
        com.hcltech.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new Exception("Service not found"));

        usageRecord.setService(service);
        return usageRecordRepository.save(usageRecord);
    }

    public UsageRecord getUsageById(Long usageId) throws Exception {
        return usageRecordRepository.findById(usageId)
            .orElseThrow(() -> new Exception("Usage record not found"));
    }

    private UsageRecordDTO convertToDTO(UsageRecord record) {
        return new UsageRecordDTO(
            record.getUsageId(),
            record.getService().getServiceId(),
            record.getUsageDate(),
            record.getUsageAmount(),
            record.getUnit()
        );
    }
}
