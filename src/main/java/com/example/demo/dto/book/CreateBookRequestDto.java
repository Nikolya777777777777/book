package com.example.demo.dto.book;

import com.example.demo.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    @NotBlank
    private String isbn;
    private String coverImage;
    private Set<Category> categoriesIds;
}
