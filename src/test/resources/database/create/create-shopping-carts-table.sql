CREATE TABLE shopping_carts (
                       user_id BIGINT NOT NULL,
                       is_deleted tinyint(1),
                       CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
