-- Bảng users
CREATE TABLE users (
                       id BIGINT,  -- tự tăng (thay cho bigint + auto_increment)
                       email VARCHAR(50) UNIQUE NOT NULL,  -- email duy nhất
                       password VARCHAR(100) NOT NULL,
                       phone_number VARCHAR(11),
                       PRIMARY KEY(id)
);

CREATE TABLE roles (
                       id BIGSERIAL,
                       role_name VARCHAR(30) NOT NULL,
                       PRIMARY KEY(id)
);
CREATE TABLE usr_role (
                          user_id BIGINT NOT NULL,
                          role_id BIGINT NOT NULL
);

alter table if exists usr_role add constraint fk_role foreign key (role_id) references roles;
alter table if exists usr_role add constraint fk_user foreign key (user_id) references users;