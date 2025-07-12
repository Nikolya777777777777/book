package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto findCategoryById(Long id);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long id);
}
