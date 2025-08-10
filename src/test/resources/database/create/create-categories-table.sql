CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            is_deleted tinyint(1) NOT NUll
);
