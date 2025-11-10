CREATE TABLE users (
                          id BIGINT,
                          email VARCHAR(200),
                          phone VARCHAR(200),
                          PRIMARY KEY (id)
);
CREATE TABLE product_variants(
                                 id BIGINT,
                                 variant_name VARCHAR(200),
                                 img_url VARCHAR(400),
                                 PRIMARY KEY (id)
);
CREATE TABLE cart_items(
                                 id BIGINT,
                                 user_id BIGINT,
                                 variant_id BIGINT,
                                 quantity INT,
                                 PRIMARY KEY (id)
);
CREATE TABLE orders(
                       id BIGINT,
                       user_id BIGINT,
                       order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total_amount BIGINT,
                       note VARCHAR(500),
                       status INT,
                       PRIMARY KEY(id)
);
CREATE TABLE items (
        id BIGINT,
        order_id BIGINT,
        variant_id BIGINT,
        quantity INT,
        price BIGINT,
        PRIMARY KEY (id)
);

alter table if exists cart_items add constraint fk_cart_usr foreign key (user_id) references users(id);
alter table if exists cart_items add constraint fk_cart_var foreign key(variant_id) references product_variants(id);
alter table if exists orders add constraint fk_ord_usr foreign key(user_id) references users(id);
alter table if exists items add constraint fk_it_ord foreign key (order_id) references orders(id);
alter table if exists items add constraint fk_it_ foreign key (variant_id) references product_variants(id);

