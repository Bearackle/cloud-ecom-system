-- Add wallet_address column to users table for Ethereum wallet integration
ALTER TABLE users ADD COLUMN wallet_address VARCHAR(42);

-- Add index for faster lookups by wallet address
CREATE INDEX idx_users_wallet_address ON users(wallet_address);

-- Add comment for documentation
COMMENT ON COLUMN users.wallet_address IS 'Ethereum wallet address (0x...) for loyalty token system';

