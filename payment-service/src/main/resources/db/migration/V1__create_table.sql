CREATE TABLE orders(
                       id BIGINT,
                       user_id BIGINT,
                       total_amount BIGINT,
                       PRIMARY KEY(id)
);
CREATE TABLE payments (
                       id BIGINT,
                       order_id BIGINT,
                       method INT,
                       status INT,
                       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (id)
);
alter table if exists payments add constraint fk_pay_ord foreign key(order_id) references orders(id);
