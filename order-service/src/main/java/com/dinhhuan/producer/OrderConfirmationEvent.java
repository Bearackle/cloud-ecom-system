package com.dinhhuan.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.dto.request.CreateOrderDto;
import com.dinhhuan.dto.request.OrderConfirmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmationEvent {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public OrderConfirmationEvent(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .queueName("confirm-email-order")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(OrderConfirmMessage order) {
        try{
            String messageBody = objectMapper.writeValueAsString(order);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            message.setContentType("application/json");
            senderClient.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
