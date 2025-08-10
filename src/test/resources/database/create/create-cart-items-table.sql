CREATE TABLE cart_items (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       shopping_cart_id BIGINT NOT NULL,
                       book_id BIGINT NOT NULL,
                       quantity INT NOT NULL,
                       CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                       CONSTRAINT fk_shopping_cart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_carts(id) ON DELETE CASCADE,
                       is_deleted tinyint(1)
);
