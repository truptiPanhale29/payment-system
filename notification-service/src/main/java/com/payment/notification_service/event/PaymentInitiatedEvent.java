package com.payment.notification_service.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiatedEvent {

    private String paymentId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
