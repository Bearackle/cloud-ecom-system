package com.dinhhuan.repository;

import com.dinhhuan.model.Address;
import com.dinhhuan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);

    Long user(User user);
}
