create table users(
                      id bigint primary key,
                      email varchar(50) unique not null,
                      full_name varchar(100) not null,
                      location varchar(30) default 'vietnam',
                      phone_number varchar(10),
                      gender varchar(10),
                      lang varchar(10) default 'vi',
                      birthday date,
                      registration_date datetime default CURRENT_TIMESTAMP,
                      updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      background_color varchar(30) default 'blue',
                      avt varchar(100)
);

create table address(
                        id bigint primary key,
                        user_id bigint,
                        address varchar(200) not null,
                        constraint fk_user foreign key (user_id) references users(id)
)