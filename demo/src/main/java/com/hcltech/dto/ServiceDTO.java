package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long serviceId;
    private Long customerId;
    private String serviceType;
    private LocalDate startDate;
    private String status;
}
