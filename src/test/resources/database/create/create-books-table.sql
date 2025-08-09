CREATE TABLE books (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       author VARCHAR(255) NOT NULL,
                       isbn VARCHAR(20) UNIQUE,
                       price DECIMAL(10, 2),
                       description VARCHAR(255),
                       cover_image VARCHAR(255),
                       is_deleted tinyint(1)
);
