package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDto {
    @NotNull
    @Min(0)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    private String isbn;
    private String description;
    private String coverImage;
}
