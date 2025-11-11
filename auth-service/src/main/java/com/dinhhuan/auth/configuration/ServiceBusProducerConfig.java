package com.dinhhuan.auth.configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBusProducerConfig {
    @Value("${spring.cloud.azure.servicebus.connection-string}")
    private String connectionString;
    @Bean
    public ServiceBusClientBuilder  serviceBusSenderClient() {
        return new ServiceBusClientBuilder()
                .connectionString(connectionString);
    }
}
