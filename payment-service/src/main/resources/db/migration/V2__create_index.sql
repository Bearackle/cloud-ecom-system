CREATE INDEX IF NOT EXISTS idx_transactions_order ON payments(order_id);
CREATE INDEX IF NOT EXISTS idx_transactions_status_created ON payments(status, created_date);