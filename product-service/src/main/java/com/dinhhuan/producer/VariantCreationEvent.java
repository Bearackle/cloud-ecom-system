package com.dinhhuan.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.dto.ProductDto;
import com.dinhhuan.dto.VariantCreation;
import com.dinhhuan.dto.VariantDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VariantCreationEvent {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public VariantCreationEvent(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .topicName("variant-topic")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(VariantDto user) {
        try{
            String messageBody = objectMapper.writeValueAsString(user);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            senderClient.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
