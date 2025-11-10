package com.dinhhuan.service;

import com.dinhhuan.dto.request.AddressRequest;
import com.dinhhuan.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {
    Long createAddress(AddressRequest addressRequest);
    AddressResponse getAddressById(Long id);
    List<AddressResponse> getAddressesByUserId(Long userId);
    List<AddressResponse> getAllAddresses();
    AddressResponse updateAddress(Long id, AddressRequest addressRequest);
    void deleteAddress(Long id);
}

