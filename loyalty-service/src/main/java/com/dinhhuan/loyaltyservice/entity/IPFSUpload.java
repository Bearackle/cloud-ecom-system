package com.dinhhuan.loyaltyservice.entity;

import jakarta. persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time. LocalDateTime;

/**
 * Entity tracking các file đã upload lên IPFS
 * 
 * Giúp:
 * - Tránh upload trùng lặp
 * - Dễ dàng tìm lại CID của file đã upload
 * - Audit và cleanup nếu cần
 */
@Entity
@Table(name = "ipfs_uploads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPFSUpload {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * IPFS Content Identifier
     * Format: Qm...  (CIDv0) hoặc bafy... (CIDv1)
     */
    @Column(name = "cid", nullable = false, unique = true, length = 100)
    private String cid;
    
    /**
     * Loại nội dung:
     * - METADATA: JSON metadata của reward
     * - IMAGE: Hình ảnh của reward
     * - CERTIFICATE: Certificate sau khi đổi thưởng
     */
    @Column(name = "content_type", nullable = false, length = 20)
    private String contentType;
    
    /**
     * Loại đối tượng liên quan:
     * - REWARD: File thuộc về một reward
     * - CERTIFICATE: File thuộc về một certificate
     */
    @Column(name = "reference_type", length = 20)
    private String referenceType;
    
    /**
     * ID của đối tượng liên quan
     */
    @Column(name = "reference_id", length = 50)
    private String referenceId;
    
    /**
     * Kích thước file (bytes)
     */
    @Column(name = "file_size")
    private Long fileSize;
    
    /**
     * MIME type
     * Ví dụ: application/json, image/png, application/pdf
     */
    @Column(name = "mime_type", length = 50)
    private String mimeType;
    
    /**
     * Thời gian upload
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime. now();
    }
}