package com.dinhhuan.consumer;

import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.dto.request.VariantCreationRequest;
import com.dinhhuan.service.UserService;
import com.dinhhuan.service.VariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerVariantHandler {
    private final VariantService variantService;
    @Bean
    public Consumer<VariantCreationRequest> consumerVariant(){
        return msg ->{
               Long id = variantService.createOrUpdate(msg);
        };
    }
}
