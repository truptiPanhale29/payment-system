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
    public PaymentResponse initiatePayment(PaymentRequest request){

        // Step 1 - Check from account exists and has sufficient balance
        AccountResponse fromAccount = restTemplate.getForObject(
                "http://localhost:8081/api/accounts/number/" + request.getFromAccount(),
                AccountResponse.class);

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new PaymentException(400, "Insufficient balance in account: " + request.getFromAccount());
        }

        // Step 2 - Check to account exists
        restTemplate.getForObject(
                "http://localhost:8081/api/accounts/number/" + request.getToAccount(),
                AccountResponse.class);

        // Step 3 - Create and save payment
        var payment = PaymentMapper.toEntity(request);
        payment.setPaymentId("PAY" + System.currentTimeMillis());
        payment.setStatus(PaymentStatus.PENDING);
        var savedPayment = paymentRepository.save(payment);

        // Step 4 - Publish event to Kafka
        PaymentInitiatedEvent event = PaymentInitiatedEvent.builder()
                .paymentId(savedPayment.getPaymentId())
                .fromAccount(savedPayment.getFromAccount())
                .toAccount(savedPayment.getToAccount())
                .amount(savedPayment.getAmount())
                .timestamp(savedPayment.getCreatedAt())
                .build();
        kafkaTemplate.send("payment-events", savedPayment.getPaymentId(),event);

        // Step 5 - Return response
        return PaymentMapper.toResponse(savedPayment);
    }


    @Override
    public PaymentResponse getPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentException(404, "Payment not found with id: " + paymentId));
        return PaymentMapper.toResponse(payment);
    }



}
