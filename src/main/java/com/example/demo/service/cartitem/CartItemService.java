package com.example.demo.service.cartitem;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.User;

public interface CartItemService {
    CartItem createCartItem(Book book, User user);
}
