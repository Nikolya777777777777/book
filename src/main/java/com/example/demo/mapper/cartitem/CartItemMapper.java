package com.example.demo.mapper.cartitem;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cartitem.CartItemRequestDto;
import com.example.demo.dto.cartitem.CartItemResponseDto;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemRequestDto toRequestDto(CartItem cartItem);

    CartItemResponseDto toResponseDto(CartItem cartItem);

    @AfterMapping
    default void setBookParameters(@MappingTarget CartItemResponseDto cartItemResponseDto,
                                   CartItem cartItem) {
        cartItemResponseDto.setBookId(cartItem.getBook().getId());
        cartItemResponseDto.setBookTitle(cartItem.getBook().getTitle());
    }

    @Mapping(target = "book", source = "bookId")
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    default Book map(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
