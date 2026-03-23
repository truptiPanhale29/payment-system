package com.payment.notification_service.consumer;

import com.payment.notification_service.event.PaymentInitiatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationConsumer {

    @KafkaListener(topics = "payment-events", groupId = "notification-service-group")
    public void handlePaymentEvent(PaymentInitiatedEvent event) {
        log.info("Sending notification for paymentId: {}", event.getPaymentId());
        log.info("Dear customer, your payment of {} from account {} to account {} has been initiated successfully.",
                event.getAmount(),event.getFromAccount(),event.getToAccount());
        log.info("Notification sent successfully for paymentId: {}", event.getPaymentId());
    }
}
