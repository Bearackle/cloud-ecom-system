package com.dinhhuan.service;

import com.dinhhuan.dto.request.AddressCreationDto;
import com.dinhhuan.dto.request.AddressDto;

import java.util.List;

public interface AddressService {
    Long create(AddressCreationDto addressCreation, Long UserId);
    void update(Long id, AddressDto address);
    void delete(Long id);
    List<AddressDto> getList(Long userId);
}
