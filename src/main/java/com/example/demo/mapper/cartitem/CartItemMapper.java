package com.example.demo.mapper.cartitem;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.dto.shoppingcart.ShoppingCartRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartRequestDtoWithoutBookId;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem updateCartItemFromDb(@MappingTarget CartItem cartItem,
                                  ShoppingCartRequestDtoWithoutBookId
                                          shoppingCartRequestDtoWithoutBookId);

    CartItemResponseDto toResponseDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId")
    CartItem shopingCartRequestDtoToCartItem(ShoppingCartRequestDto shoppingCartRequestDto);

    default Book map(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
