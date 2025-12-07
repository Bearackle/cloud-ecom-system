package com.dinhhuan.loyaltyservice.repository;

import org.springframework.data. jpa.repository. JpaRepository;
import org.springframework. stereotype.Repository;

import com.dinhhuan.loyaltyservice.entity.IPFSUpload;

import java.util. List;
import java.util. Optional;

/**
 * Repository cho IPFSUpload
 */
@Repository
public interface IPFSUploadRepository extends JpaRepository<IPFSUpload, Long> {
    
    /**
     * Tìm upload theo CID
     */
    Optional<IPFSUpload> findByCid(String cid);
    
    /**
     * Tìm tất cả upload theo reference
     */
    List<IPFSUpload> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);
    
    /**
     * Kiểm tra CID đã tồn tại chưa
     */
    boolean existsByCid(String cid);
}