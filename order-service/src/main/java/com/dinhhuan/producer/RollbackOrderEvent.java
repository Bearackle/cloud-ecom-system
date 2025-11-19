package com.dinhhuan.producer;


import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.dto.request.CreateOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class RollbackOrderEvent {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public RollbackOrderEvent(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
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
            message.getApplicationProperties().put("eventType", "OrderCanceled");
            message.getApplicationProperties().put("sagaStep", "DESTROY_ORDER");
            message.getApplicationProperties().put("source", "order-service");
            senderClient.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
