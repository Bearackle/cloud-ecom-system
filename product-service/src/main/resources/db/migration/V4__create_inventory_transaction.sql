CREATE TABLE inventory_transactions (
                          id BIGINT,
                          variant_id BIGINT NOT NULL,
                          order_id BIGINT,
                          quantity INT,
                          PRIMARY KEY (id)
);
alter table if exists inventory_transactions add constraint fk_iv_var foreign key (variant_id) references product_variants(id);
