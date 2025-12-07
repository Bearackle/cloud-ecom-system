# 🧪 HƯỚNG DẪN TEST LOYALTY SERVICE

## ✅ XÁC NHẬN SERVICE ĐÃ CHẠY THÀNH CÔNG

Bạn đã test và thấy:

```json
{
  "success": true,
  "data": {
    "walletAddress": "0x0000000000000000000000000000000000000000",
    "registered": false,
    "tokenBalance": 0,
    "certificateCount": 0
  }
}
```

**→ Service đang chạy thành công! ✅**

---

## 📍 CÁC ENDPOINT ĐỂ TEST

### 1. Health Check (Sau khi restart service)

```bash
# Ping đơn giản
curl http://localhost:8308/health/ping

# Health check chi tiết
curl http://localhost:8308/health
```

### 2. Test Customer APIs

```bash
# Lấy thông tin customer (đã test thành công)
curl http://localhost:8308/api/v1/customers/0xYOUR_WALLET_ADDRESS

# Đăng ký customer mới
curl -X POST http://localhost:8308/api/v1/customers/register \
  -H "Content-Type: application/json" \
  -d '{"walletAddress": "0xYOUR_WALLET_ADDRESS"}'

# Lấy số dư token
curl http://localhost:8308/api/v1/customers/0xYOUR_WALLET_ADDRESS/balance
```

### 3. Test IPFS APIs

````bash
# Upload JSON lên IPFS
curl -X POST http://localhost:8308/api/v1/ipfs/upload/json \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "name": "Test Voucher",
      "description": "Test description"
    },
    "name": "test_metadata"
  }'

# Lấy data từ IPFS (thay CID bằng CID vừa upload)
curl http://localhost:8308/api/v1/ipfs/QmYOUR_CID_HERE
```QmS3vf4sfKpnw8RceXLPFD5jQtH1WLVSptSi9Dyrs5dExN

### 4. Test Reward APIs

```bash
# Tạo reward mới
curl -X POST http://localhost:8308/api/v1/rewards \
  -H "Content-Type: application/json" \
  -d '{
    "rewardId": 1,
    "cost": 100,
    "metadata": {
      "name": "Voucher giảm 10%",
      "description": "Áp dụng cho đơn từ 500k"
    }
  }'
  (cái này chạy hơi lâu)

# Lấy thông tin reward
curl http://localhost:8308/api/v1/rewards/1
````

---

## 🎯 TEST WORKFLOW ĐẦY ĐỦ

### Bước 1: Lấy địa chỉ ví từ MetaMask

1. Mở MetaMask extension
2. Chuyển sang Sepolia testnet
3. Copy địa chỉ ví (0x...)
4. Export biến:

```bash
export WALLET="0xYOUR_WALLET_ADDRESS"
```

### Bước 2: Đăng ký Customer

```bash
curl -X POST http://localhost:8308/api/v1/customers/register \
  -H "Content-Type: application/json" \
  -d "{\"walletAddress\": \"$WALLET\"}"
```

**Expected Response:**

```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "walletAddress": "0x...",
    "transactionHash": "0x...",
    "blockNumber": 12345678
  }
}
```

**✅ Verify trên Etherscan:**

- Copy `transactionHash`
- Mở: https://sepolia.etherscan.io/tx/0x...
- Xem transaction đã được confirm

### Bước 3: Phát hành Token

```bash
curl -X POST http://localhost:8308/api/v1/customers/$WALLET/tokens/issue \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 100,
    "orderId": "ORD-TEST-001",
    "reason": "PURCHASE"
  }'
```

**Expected Response:**

```json
{
  "success": true,
  "message": "Tokens issued successfully",
  "data": {
    "walletAddress": "0x...",
    "tokensIssued": 100,
    "newBalance": 100.0,
    "transactionHash": "0x...",
    "orderId": "ORD-TEST-001"
  }
}
```

**✅ Verify:**

- Kiểm tra số dư trên MetaMask (phải thấy 100 tokens)
- Verify transaction trên Etherscan

### Bước 4: Kiểm tra số dư

```bash
curl http://localhost:8308/api/v1/customers/$WALLET/balance
```

**Expected:**

```json
{
  "success": true,
  "data": 100.0
}
```

### Bước 5: Test IPFS Upload

```bash
curl -X POST http://localhost:8308/api/v1/ipfs/upload/json \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "name": "Test Voucher",
      "description": "This is a test"
    },
    "name": "test_metadata"
  }'
```

**Expected Response:**

```json
{
  "success": true,
  "message": "JSON uploaded successfully",
  "data": {
    "ipfsCid": "Qm...",
    "ipfsUrl": "https://gateway.pinata.cloud/ipfs/Qm...",
    "mimeType": "application/json"
  }
}
```

**✅ Verify:**

- Copy `ipfsUrl` và mở trong browser
- Phải thấy JSON data

### Bước 6: Tạo Reward

```bash
curl -X POST http://localhost:8308/api/v1/rewards \
  -H "Content-Type: application/json" \
  -d '{
    "rewardId": 1,
    "cost": 50,
    "metadata": {
      "name": "Voucher giảm 10%",
      "description": "Áp dụng cho đơn từ 500k",
      "terms": "Không áp dụng cùng KM khác"
    }
  }'
```

**Expected Response:**

```json
{
  "success": true,
  "message": "Reward created successfully",
  "data": {
    "rewardId": 1,
    "transactions": {
      "setRewardCost": "0x...",
      "setRewardMetadata": "0x..."
    },
    "ipfs": {
      "metadataCid": "Qm...",
      "metadataUrl": "https://gateway.pinata.cloud/ipfs/Qm..."
    }
  }
}
```

---

## 🛠️ SỬ DỤNG POSTMAN HOẶC INSOMNIA

Nếu không thích dùng curl, bạn có thể dùng Postman hoặc Insomnia:

### Import Collection vào Postman:

1. Tạo Collection mới tên "Loyalty Service"
2. Tạo các request sau:

**Request 1: Health Check**

- Method: GET
- URL: `http://localhost:8308/health/ping`

**Request 2: Register Customer**

- Method: POST
- URL: `http://localhost:8308/api/v1/customers/register`
- Headers: `Content-Type: application/json`
- Body (raw JSON):

```json
{
  "walletAddress": "0xYOUR_WALLET_ADDRESS"
}
```

**Request 3: Issue Tokens**

- Method: POST
- URL: `http://localhost:8308/api/v1/customers/{{walletAddress}}/tokens/issue`
- Headers: `Content-Type: application/json`
- Body (raw JSON):

```json
{
  "amount": 100,
  "orderId": "ORD-001",
  "reason": "PURCHASE"
}
```

**Request 4: Get Balance**

- Method: GET
- URL: `http://localhost:8308/api/v1/customers/{{walletAddress}}/balance`

**Request 5: Upload IPFS**

- Method: POST
- URL: `http://localhost:8308/api/v1/ipfs/upload/json`
- Headers: `Content-Type: application/json`
- Body (raw JSON):

```json
{
  "data": {
    "name": "Test",
    "description": "Test description"
  },
  "name": "test_metadata"
}
```

---

## 📊 CHECKLIST TEST

Sau khi test, đảm bảo:

- [ ] `/health/ping` trả về `{"status":"pong"}`
- [ ] `/health` hiển thị blockchain status
- [ ] Đăng ký customer thành công (có transaction hash)
- [ ] Phát hành token thành công (có transaction hash)
- [ ] Số dư token tăng đúng
- [ ] Upload IPFS thành công (có CID)
- [ ] Có thể retrieve data từ IPFS
- [ ] Tạo reward thành công
- [ ] Transaction có thể verify trên Etherscan

---

## 🐛 TROUBLESHOOTING

### Lỗi: "Customer not registered"

→ Gọi API register trước

### Lỗi: "Transaction failed"

→ Kiểm tra:

- Ví owner có đủ ETH Sepolia không?
- Contract address đúng chưa?
- Private key đúng chưa?

### Lỗi: "IPFS upload failed"

→ Kiểm tra:

- `PINATA_SECRET_KEY` đúng chưa (phải là JWT token)
- Internet connection

### Lỗi: "Connection refused"

→ Service chưa start hoặc đang chạy ở port khác

---

## 🎓 NEXT STEPS

Sau khi test thành công:

1. Tích hợp với Order Service để tự động phát hành token
2. Tích hợp với Frontend để hiển thị rewards
3. Test workflow đổi thưởng → nhận voucher
