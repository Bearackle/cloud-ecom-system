package com.dinhhuan.auth.producer;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreationProducer {
    private ServiceBusSenderClient senderClient;
    private ObjectMapper objectMapper;
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
