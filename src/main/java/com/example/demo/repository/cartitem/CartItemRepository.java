package com.example.demo.repository.cartitem;

import com.example.demo.model.CartItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long cartId, Long bookId);

    Optional<Set<CartItem>> getAllCartItemsByShoppingCartId(Long shoppingCartId);

}
