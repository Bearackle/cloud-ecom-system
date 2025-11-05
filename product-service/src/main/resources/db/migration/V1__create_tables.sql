CREATE TABLE products (
    id BIGINT,
    product_name VARCHAR(200) NOT NULL ,
    price BIGINT,
    active INT,
    category_id BIGINT NOT NULL,
    brand_id BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    PRIMARY KEY (id)
);
CREATE TABLE product_variants(
    id BIGINT,
    product_id BIGINT NOT NULL,
    product_variant_name VARCHAR(200),
    quantity INT,
    PRIMARY KEY (id)
);
CREATE TABLE product_attributes(
    id BIGINT,
    product_id BIGINT NOT NULL,
    attribute_name VARCHAR(200),
    attribute_value VARCHAR(200),
    PRIMARY KEY (id)
);
CREATE TABLE categories(
    id BIGINT,
    category_name VARCHAR(200),
    parent_id BIGINT,
    img_url VARCHAR(300),
    PRIMARY KEY (id)
);
CREATE TABLE brands(
    id BIGINT,
    brand_name VARCHAR(100),
    avt_img_url VARCHAR(300),
    PRIMARY KEY(id)
);
CREATE TABLE product_imgs(
    id BIGINT,
    product_id BIGINT NOT NULL,
    img_url VARCHAR(300),
    PRIMARY KEY (id)
);

alter table if exists product_variants add constraint fk_var_pr foreign key (product_id) references products(id);
alter table if exists products add constraint fk_pr_br foreign key(brand_id) references brands(id);
alter table if exists products add constraint fk_pr_ct foreign key(category_id) references categories(id);
alter table if exists product_imgs add constraint fk_img_pr foreign key (product_id) references products(id);
alter table if exists product_attributes add constraint fk_attr_pr foreign key (product_id) references products(id);
alter table if exists categories add constraint fk_ct_par_ct foreign key(parent_id) references categories(id);


