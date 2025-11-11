package com.dinhhuan.auth.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreationProducer {
    private final ServiceBusSenderClient  senderClient;
    private final ObjectMapper objectMapper;
    public UserCreationProducer(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .topicName("user-topic")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(UserRegistrationDto user) {
        try{
            String messageBody = objectMapper.writeValueAsString(user);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            senderClient.sendMessage(message);
            log.info("Message sent successfully: {}", user.getFullName());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
