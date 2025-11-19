package com.dinhhuan.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentSuccessProducer {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public PaymentSuccessProducer(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .topicName("order-saga")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(Long orderId) {
        try{
            String messageBody = objectMapper.writeValueAsString(orderId);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            message.getApplicationProperties().put("eventType", "PaymentSuccess");
            message.getApplicationProperties().put("sagaStep", "WAITING_INVENTORY");
            message.getApplicationProperties().put("source", "payment-service");
            senderClient.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
