-- ===== ENUMS =====
DO $$
BEGIN
   IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'order_status') THEN
CREATE TYPE order_status AS ENUM ('PENDING','PAID','SHIPPED','CANCELED');
END IF;
END $$;

-- ===== TABLES =====

-- users
CREATE TABLE IF NOT EXISTS users (
                                     id           BIGSERIAL PRIMARY KEY,
                                     email        VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) UNIQUE
    );

-- product_variant
CREATE TABLE IF NOT EXISTS product_variant (
                                               id         BIGSERIAL PRIMARY KEY,
                                               name       VARCHAR(200)  NOT NULL,
    img_url    VARCHAR(500),
    created_at TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
    );

-- orders
CREATE TABLE IF NOT EXISTS orders (
                                      id            BIGSERIAL PRIMARY KEY,
                                      user_id       BIGINT      NOT NULL,
                                      total_amount  NUMERIC(12,2) NOT NULL,
    note          TEXT,
    status        order_status NOT NULL DEFAULT 'PENDING',
    order_date    TIMESTAMP    NOT NULL DEFAULT now(),
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP,
    CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- item
CREATE TABLE IF NOT EXISTS item (
                                    id                BIGSERIAL PRIMARY KEY,
                                    order_id          BIGINT    NOT NULL,
                                    product_variant_id BIGINT   NOT NULL,
                                    quantity          INTEGER   NOT NULL DEFAULT 1,
                                    price             NUMERIC(12,2),
    CONSTRAINT fk_item_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_product_variant
    FOREIGN KEY (product_variant_id) REFERENCES product_variant(id)
    );

-- cart_items
CREATE TABLE IF NOT EXISTS cart_items (
                                          id                BIGSERIAL PRIMARY KEY,
                                          user_id           BIGINT    NOT NULL,
                                          product_variant_id BIGINT   NOT NULL,
                                          quantity          INTEGER   NOT NULL DEFAULT 1,
                                          CONSTRAINT uq_cart_user_variant UNIQUE (user_id, product_variant_id),
    CONSTRAINT fk_cart_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_product_variant
    FOREIGN KEY (product_variant_id) REFERENCES product_variant(id)
    );

-- ===== INDEXES =====
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_item_order ON item(order_id);
CREATE INDEX IF NOT EXISTS idx_item_product_variant ON item(product_variant_id);
CREATE INDEX IF NOT EXISTS idx_cart_user ON cart_items(user_id);
CREATE INDEX IF NOT EXISTS idx_cart_product_variant ON cart_items(product_variant_id);
