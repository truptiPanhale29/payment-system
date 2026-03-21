package com.payment.payment_service.controller;

import com.payment.payment_service.dto.PaymentRequest;
import com.payment.payment_service.dto.PaymentResponse;
import com.payment.payment_service.entity.Payment;
import com.payment.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponse initiatePayment(@RequestBody @Valid PaymentRequest request) {
        return paymentService.initiatePayment(request);
    }

    @GetMapping("/{paymentId}")
    public PaymentResponse getPayment(@PathVariable("paymentId") String paymentId) {
        return paymentService.getPayment(paymentId);
    }
}
