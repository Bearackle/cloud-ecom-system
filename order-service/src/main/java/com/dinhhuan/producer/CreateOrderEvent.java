package com.dinhhuan.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.dto.request.CreateOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderEvent {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public CreateOrderEvent(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .topicName("order-saga")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(CreateOrderDto order) {
        try{
            String messageBody = objectMapper.writeValueAsString(order);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            message.getApplicationProperties().put("eventType", "OrderCreated");
            message.getApplicationProperties().put("sagaStep", "WAITING_PAYMENT");
            message.getApplicationProperties().put("source", "order-service");
            senderClient.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
