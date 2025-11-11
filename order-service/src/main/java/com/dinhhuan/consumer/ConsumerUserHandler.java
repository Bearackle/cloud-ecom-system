package com.dinhhuan.consumer;


import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerUserHandler {
    private final UserService userService;
    @Bean
    public Consumer<UserRegistrationDto> consumerRegistration(){
        return msg ->{
            Long id = userService.createOrUpdate(msg);
        };
    }
}
