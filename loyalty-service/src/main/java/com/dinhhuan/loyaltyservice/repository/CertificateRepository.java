package com.dinhhuan.loyaltyservice.repository;

import org.springframework.data. jpa.repository. JpaRepository;
import org.springframework. stereotype.Repository;

import com.dinhhuan.loyaltyservice.entity.Certificate;

import java.util. List;
import java.util. Optional;

/**
 * Repository cho Certificate
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    
    /**
     * Tìm certificate theo mã voucher
     */
    Optional<Certificate> findByVoucherCode(String voucherCode);
    
    /**
     * Tìm tất cả certificate của một khách hàng
     */
    List<Certificate> findByCustomerAddress(String customerAddress);
    
    /**
     * Tìm certificate chưa sử dụng của khách hàng
     */
    List<Certificate> findByCustomerAddressAndRedeemedFalse(String customerAddress);
    
    /**
     * Kiểm tra voucher code đã tồn tại chưa
     */
    boolean existsByVoucherCode(String voucherCode);
}