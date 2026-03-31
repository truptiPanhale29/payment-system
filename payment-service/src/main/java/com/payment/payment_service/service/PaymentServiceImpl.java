package com.payment.payment_service.service;

import com.payment.payment_service.dto.AccountResponse;
import com.payment.payment_service.dto.PaymentRequest;
import com.payment.payment_service.dto.PaymentResponse;
import com.payment.payment_service.entity.Payment;
import com.payment.payment_service.entity.PaymentStatus;
import com.payment.payment_service.event.PaymentInitiatedEvent;
import com.payment.payment_service.exception.PaymentException;
import com.payment.payment_service.mapper.PaymentMapper;
import com.payment.payment_service.repository.PaymentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, PaymentInitiatedEvent> kafkaTemplate;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              RestTemplate restTemplate,
                              KafkaTemplate<String, PaymentInitiatedEvent> kafkaTemplate)
    {
        this.paymentRepository = paymentRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;

    }

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {

        // Step 1 - Validate sender account
        AccountResponse fromAccount = restTemplate.getForObject(
                "http://localhost:8081/api/accounts/number/" + request.getFromAccount(),
                AccountResponse.class);

        if (fromAccount == null) {
            throw new PaymentException(404, "Sender account not found: " + request.getFromAccount());
        }
        if (!fromAccount.getStatus().equals("ACTIVE")) {
            throw new PaymentException(400, "Sender account is not active: " + request.getFromAccount());
        }
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new PaymentException(400, "Insufficient balance in account: " + request.getFromAccount());
        }

        // Step 2 - Validate receiver account
        AccountResponse toAccount = restTemplate.getForObject(
                "http://localhost:8081/api/accounts/number/" + request.getToAccount(),
                AccountResponse.class);

        if (toAccount == null) {
            throw new PaymentException(404, "Receiver account not found: " + request.getToAccount());
        }
        if (!toAccount.getStatus().equals("ACTIVE")) {
            throw new PaymentException(400, "Receiver account is not active: " + request.getToAccount());
        }

        // Step 3 - Withdraw from sender
        restTemplate.put(
                "http://localhost:8081/api/accounts/" + fromAccount.getId() + "/withdraw?amount=" + request.getAmount(),
                null);

        // Step 4 - Deposit to receiver with compensation on failure
        try {
            restTemplate.put(
                    "http://localhost:8081/api/accounts/" + toAccount.getId() + "/deposit?amount=" + request.getAmount(),
                    null);
        } catch (Exception e) {
            // Compensate - reverse the withdrawal
            restTemplate.put(
                    "http://localhost:8081/api/accounts/" + fromAccount.getId() + "/deposit?amount=" + request.getAmount(),
                    null);
            throw new PaymentException(500, "Payment failed during deposit. Withdrawal has been reversed.");
        }

        // Step 5 - Save payment as COMPLETED
        var payment = PaymentMapper.toEntity(request);
        payment.setPaymentId("PAY" + System.currentTimeMillis());
        payment.setStatus(PaymentStatus.COMPLETED);
        var savedPayment = paymentRepository.save(payment);

        // Step 6 - Publish event to Kafka
        PaymentInitiatedEvent event = PaymentInitiatedEvent.builder()
                .paymentId(savedPayment.getPaymentId())
                .fromAccount(savedPayment.getFromAccount())
                .toAccount(savedPayment.getToAccount())
                .amount(savedPayment.getAmount())
                .timestamp(savedPayment.getCreatedAt())
                .build();
        kafkaTemplate.send("payment-events", savedPayment.getPaymentId(), event);

        // Step 7 - Return response
        return PaymentMapper.toResponse(savedPayment);
    }


    @Override
    public PaymentResponse getPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentException(404, "Payment not found with id: " + paymentId));
        return PaymentMapper.toResponse(payment);
    }



}
