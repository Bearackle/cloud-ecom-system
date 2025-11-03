CREATE TABLE products (
    id BIGINT,
    product_name VARCHAR(200) NOT NULL ,
    price BIGINT,
    active INT,
    category_id BIGINT,
    brand_id BIGINT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    PRIMARY KEY (id)
)
CREATE TABLE product_attributes(
    id BIGINT,
    product_id BIGINT,
    attribute_name VARCHAR(200),
    attribute_value VARCHAR(200)
)
CREATE TABLE categories(
    id BIGINT,
    category_name VARCHAR(200),
    parent_id BIGINT,
    img_url VARCHAR(300)
    PRIMARY KEY (id)
)
CREATE TABLE brands(
    id BIGINT,
    brand_name VARCHAR(100)
    avt_img_url VARCHAR(300)
    PRIMARY KEY(id)
)
CREATE TABLE product_img(
    id BIGINT,
    product_id BIGINT,
    img_urls VARCHAR(300),
    PRIMARY KEY (id)
)