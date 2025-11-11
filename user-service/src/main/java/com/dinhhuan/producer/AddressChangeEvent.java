package com.dinhhuan.producer;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.dto.request.AddressSyncDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddressChangeEvent {
    private final ServiceBusSenderClient senderClient;
    private final ObjectMapper objectMapper;
    public AddressChangeEvent(ServiceBusClientBuilder builder, ObjectMapper objectMapper) {
        this.senderClient = builder
                .sender()
                .topicName("address-topic")
                .buildClient();
        this.objectMapper = objectMapper;
    }
    public void sendMessage(AddressSyncDto address) {
        try{
            String messageBody = objectMapper.writeValueAsString(address);
            ServiceBusMessage message = new ServiceBusMessage(messageBody);
            senderClient.sendMessage(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
