package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsageRecordDTO {
    private Long usageId;
    private Long serviceId;
    private LocalDate usageDate;
    private BigDecimal usageAmount;
    private String unit;
}
