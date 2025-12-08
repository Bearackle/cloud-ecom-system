CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status_created ON orders(status);
CREATE INDEX IF NOT EXISTS idx_cart_items_user ON cart_items(user_id);