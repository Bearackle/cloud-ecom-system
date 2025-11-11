package com.dinhhuan.consumer;

import com.dinhhuan.commons.auth.UserRegistrationDto;
import com.dinhhuan.dto.request.AddressSyncDto;
import com.dinhhuan.service.AddressService;
import com.dinhhuan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ConsumerAddressHandler {
    private final AddressService addressService;
    @Bean
    public Consumer<AddressSyncDto> consumerAddress(){
        return msg ->{
            Long id = addressService.createOrUpdate(msg);
        };
    }
}
