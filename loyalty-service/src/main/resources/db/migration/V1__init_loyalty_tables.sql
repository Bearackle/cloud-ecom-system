-- ============================================
-- LOYALTY SERVICE DATABASE SCHEMA
-- ============================================

-- Bảng lưu trữ các giao dịch token (issue/redeem)
CREATE TABLE token_transactions (
                                    id BIGSERIAL PRIMARY KEY,

    -- Địa chỉ ví khách hàng (42 ký tự: 0x + 40 hex)
                                    customer_address VARCHAR(42) NOT NULL,

    -- Số lượng token (dạng decimal để lưu chính xác)
                                    amount DECIMAL(36, 18) NOT NULL,

    -- Loại giao dịch: ISSUE (phát hành) hoặc REDEEM (đổi thưởng)
                                    transaction_type VARCHAR(20) NOT NULL,

    -- ID đơn hàng (nếu là ISSUE từ việc mua hàng)
                                    order_id VARCHAR(50),

    -- ID phần thưởng (nếu là REDEEM)
                                    reward_id INTEGER,

    -- Transaction hash trên blockchain (66 ký tự: 0x + 64 hex)
                                    tx_hash VARCHAR(66) NOT NULL,

    -- Block number khi transaction được confirm
                                    block_number BIGINT,

    -- Thời gian tạo
                                    created_at TIMESTAMP DEFAULT NOW(),

    -- Indexes để tối ưu query
                                    CONSTRAINT chk_transaction_type CHECK (transaction_type IN ('ISSUE', 'REDEEM'))
);

-- Index để tìm kiếm nhanh theo customer và order
CREATE INDEX idx_token_tx_customer ON token_transactions(customer_address);
CREATE INDEX idx_token_tx_order ON token_transactions(order_id);
CREATE INDEX idx_token_tx_type ON token_transactions(transaction_type);

-- ============================================
-- Bảng lưu trữ certificates (voucher sau khi đổi thưởng)
-- ============================================
CREATE TABLE certificates (
                              id BIGSERIAL PRIMARY KEY,

    -- Địa chỉ ví khách hàng
                              customer_address VARCHAR(42) NOT NULL,

    -- ID phần thưởng đã đổi
                              reward_id BIGINT NOT NULL,

    -- Mã voucher unique (format: VOUCHER-XXXXXXXX)
                              voucher_code VARCHAR(20) UNIQUE NOT NULL,

    -- IPFS CID của certificate metadata
                              ipfs_cid VARCHAR(100) NOT NULL,

    -- Hash để verify certificate (32 ký tự hex)
                              verification_hash VARCHAR(64) NOT NULL,

    -- Transaction hash khi issue certificate
                              tx_hash VARCHAR(66) NOT NULL,

    -- Trạng thái đã sử dụng chưa
                              redeemed BOOLEAN DEFAULT FALSE,

    -- Thời gian sử dụng voucher
                              redeemed_at TIMESTAMP,

    -- Thời gian tạo
                              created_at TIMESTAMP DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_cert_customer ON certificates(customer_address);
CREATE INDEX idx_cert_voucher ON certificates(voucher_code);
CREATE INDEX idx_cert_redeemed ON certificates(redeemed);

-- ============================================
-- Bảng lưu trữ thông tin các file đã upload lên IPFS
-- ============================================
CREATE TABLE ipfs_uploads (
                              id BIGSERIAL PRIMARY KEY,

    -- IPFS CID (Content Identifier)
                              cid VARCHAR(100) UNIQUE NOT NULL,

    -- Loại nội dung: METADATA, IMAGE, CERTIFICATE
                              content_type VARCHAR(20) NOT NULL,

    -- Loại đối tượng liên quan: REWARD, CERTIFICATE
                              reference_type VARCHAR(20),

    -- ID của đối tượng liên quan
                              reference_id VARCHAR(50),

    -- Kích thước file (bytes)
                              file_size BIGINT,

    -- MIME type của file
                              mime_type VARCHAR(50),

    -- Thời gian upload
                              created_at TIMESTAMP DEFAULT NOW(),

                              CONSTRAINT chk_content_type CHECK (content_type IN ('METADATA', 'IMAGE', 'CERTIFICATE'))
);

-- Index
CREATE INDEX idx_ipfs_cid ON ipfs_uploads(cid);
CREATE INDEX idx_ipfs_reference ON ipfs_uploads(reference_type, reference_id);