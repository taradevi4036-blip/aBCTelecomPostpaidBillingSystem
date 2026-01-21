package com.hcltech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long invoiceId;
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
}
