CREATE TABLE addresses (
                           id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           address VARCHAR(200) NOT NULL,
                           PRIMARY KEY (id)
);
CREATE TABLE users (
                       id BIGINT,
                       email VARCHAR(50) UNIQUE NOT NULL,
                       full_name VARCHAR(100) NOT NULL,
                       location VARCHAR(30) DEFAULT 'vietnam',
                       phone_number VARCHAR(10),
                       lang VARCHAR(10) DEFAULT 'vi',
                       registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (id)
);

alter table if exists addresses add constraint fk_address_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE