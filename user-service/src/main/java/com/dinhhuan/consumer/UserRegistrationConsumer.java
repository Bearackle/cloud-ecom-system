package com.dinhhuan.consumer;

import com.azure.messaging.servicebus.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationConsumer {
    @Value("${spring.azure.servicebus.connection-string}")
    private String connectionString;
    @Value("${spring.azure.servicebus.queue.name}")
    private String queueName;
    @PostConstruct
    public void init() {
        try (ServiceBusProcessorClient processor = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .processor()
                .queueName(queueName)
                .processMessage(this::handleMessage)
                .processError(this::handleError)
                .maxConcurrentCalls(5)
                .buildProcessorClient()){
            processor.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void handleMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.println("Received message: " + message.getBody().toString());
        context.complete();
    }

    private void handleError(ServiceBusErrorContext context) {
        System.err.println("Error while receiving message: " + context.getException());
    }
}
