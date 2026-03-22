package com.payment.transaction_service.consumer;

import com.payment.transaction_service.entity.Transaction;
import com.payment.transaction_service.entity.TransactionStatus;
import com.payment.transaction_service.event.PaymentInitiatedEvent;
import com.payment.transaction_service.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentEventConsumer {

    private final TransactionRepository transactionRepository;

    public PaymentEventConsumer(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @KafkaListener(topics="payment-events",groupId = "transaction-service-group")
    public void handlePaymentEvent(PaymentInitiatedEvent event) {
        log.info("Received PaymentEvent for paymentId: {}", event.getPaymentId());

        Transaction ttransaction= Transaction.builder()
                .paymentId(event.getPaymentId())
                .fromAccountId(event.getFromAccount())
                .toAccountId(event.getToAccount())
                .amount(event.getAmount())
                .status(TransactionStatus.Success)
                .build();

        transactionRepository.save(ttransaction);

        log.info("Transaction saved successfully for paymentId: {}", event.getPaymentId());
    }
}
