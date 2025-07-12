package com.example.demo.service.category.impl;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.mapper.category.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        return categoryMapper.toResponseDto(categoryRepository
                .save(categoryMapper.toEntity(categoryRequestDto)));
    }

    @Override
    public CategoryResponseDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found by id: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category was not found by id: " + id));
        category = categoryMapper.updateCategoryFromDb(categoryRequestDto, category);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category was not found by id: " + id));
        categoryRepository.deleteById(id);
    }
}
