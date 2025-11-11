package com.dinhhuan.service.impl;

import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.dinhhuan.dto.request.AddressCreationDto;
import com.dinhhuan.dto.request.AddressDto;
import com.dinhhuan.dto.request.AddressSyncDto;
import com.dinhhuan.model.Address;
import com.dinhhuan.model.User;
import com.dinhhuan.producer.AddressChangeEvent;
import com.dinhhuan.repository.AddressRepository;
import com.dinhhuan.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final DefaultUidGenerator uid;
    private final AddressChangeEvent addressChangeEvent;
    @Override
    public Long create(AddressCreationDto addressCreation, Long UserId) {
        var address = new Address();
        address.setId(uid.getUID());
        address.setUser(User.builder().id(UserId).build());
        address.setAddress(addressCreation.getLocation());
        addressRepository.save(address);
        addressChangeEvent.sendMessage(AddressSyncDto.builder()
                .id(address.getId())
                .userId(UserId)
                .location(address.getAddress())
                .build());
        return address.getId();
    }
    @Override
    public void update(Long id , AddressDto address) {
        var addressEntity = addressRepository.findById(id).orElse(null);
        if(addressEntity != null){
            addressEntity.setUser(User.builder().id(address.getId()).build());
            addressEntity.setAddress(address.getLocation());
            addressRepository.save(addressEntity);
            addressChangeEvent.sendMessage(AddressSyncDto.builder().id(address.getId())
                    .userId(address.getUserId())
                    .location(address.getLocation())
                    .build());
        }
    }

    @Override
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public List<AddressDto> getList(Long userId) {
        return addressRepository.findByUserId(userId)
                .stream().map(
                        a -> AddressDto.builder().id(a.getId())
                                .userId(a.getUser().getId())
                                .location(a.getAddress())
                                .build()
                ).toList();
    }
}
