package com.example.demo.mapper.cartitem;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemRequestDto toRequestDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId")
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    default Book map(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
