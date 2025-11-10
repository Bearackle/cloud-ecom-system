package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.common.AppEx;
import com.dinhhuan.dto.request.AddressRequest;
import com.dinhhuan.dto.response.AddressResponse;
import com.dinhhuan.model.Address;
import com.dinhhuan.model.User;
import com.dinhhuan.repository.AddressRepository;
import com.dinhhuan.repository.UserRepository;
import com.dinhhuan.service.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final DefaultUidGenerator uidGenerator;

    @Override
    @Transactional
    public Long createAddress(AddressRequest request) {
        User user = findUser(request.getUserId());
        Address address = Address.builder()
                .id(uidGenerator.getUID())
                .user(user)
                .location(request.getLocation())
                .build();
        return addressRepository.save(address).getId();
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        return toDto(findAddress(id));
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Address address = findAddress(id);
        if (request.getUserId() != null) {
            address.setUser(findUser(request.getUserId()));
        }
        if (request.getLocation() != null) address.setLocation(request.getLocation());
        return toDto(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    private Address findAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new AppEx("Address not found: " + id, HttpStatus.NOT_FOUND));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppEx("User not found: " + id, HttpStatus.NOT_FOUND));
    }

    private AddressResponse toDto(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .userId(address.getUser().getId())
                .location(address.getLocation())
                .build();
    }
}
