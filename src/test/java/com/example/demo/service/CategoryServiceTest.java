package com.example.demo.service;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.category.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
			Create new category and wait for returning responseDto
			""")
    public void createCategory_WithValidRequest_ReturnCategoryResponseDto() {
        Category category = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");
       CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
               .setName("Detective")
               .setDescription("Interesting satisfaction stories about spies and good boys");

       CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
               .setName("Detective")
               .setId(1L)
               .setDescription("Interesting satisfaction stories about spies and good boys");

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto responseDto = categoryService.createCategory(categoryRequestDto);

        assertThat(responseDto.getName()).isEqualTo(category.getName());
        verify(categoryMapper).toEntity(categoryRequestDto);
        verify(categoryMapper).toResponseDto(category);
        verify(categoryRepository).save(category);
        verifyNoMoreInteractions(categoryRepository,  categoryMapper);
    }

    @Test
    @DisplayName("""
			Find category by id
			""")
    public void findCategory_ById_ReturnCategoryResponseDto() {
        Category category = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setName("Detective")
                .setId(1L)
                .setDescription("Interesting satisfaction stories about spies and good boys");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto responseDto = categoryService.findCategoryById(category.getId());

        assertThat(responseDto.getName()).isEqualTo(category.getName());
        verify(categoryMapper).toResponseDto(category);
        verify(categoryRepository).findById(category.getId());
        verifyNoMoreInteractions(categoryRepository,  categoryMapper);
    }

    @Test
    @DisplayName("""
			Find category by invalid id and throw exception
			""")
    public void findCategory_ByInvalidId_ShouldThrowException() {
        Long categoryId = 3L;

        String expected = "Category not found by id: " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findCategoryById(categoryId)
        );

        assertThat(exception.getMessage()).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
			Update category by id and by given params
			""")
    public void updateCategory_ByIdAndCategoryResponseDto_ReturnCategoryResponseDto() {
        Category category = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Comic")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setName("Comic")
                .setId(1L)
                .setDescription("Interesting satisfaction stories about spies and good boys");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryResponseDto responseDto = categoryService.updateCategory(category.getId(), categoryRequestDto);

        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        verify(categoryMapper).toResponseDto(category);
        verify(categoryMapper).updateCategoryFromDb(categoryRequestDto, category);
        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(category);
        verifyNoMoreInteractions(categoryRepository,  categoryMapper);
    }

    @Test
    @DisplayName("""
			Update category with invalid id and wait to throw exception
			""")
    public void updateCategory_WithInvalidId_ShouldThrowException() {
        Long categoryId = 3L;

        String expected = "Category not found by id: " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findCategoryById(categoryId)
        );

        assertThat(exception.getMessage()).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
			Delete category by id and by given params
			""")
    public void deleteCategory_ById_ReturnNothing() {
        Category category = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        categoryService.deleteCategory(category.getId());

        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).deleteById(category.getId());
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
			Delete category with invalid id and wait to throw exception
			""")
    public void deleteCategory_WithInvalidId_ShouldThrowException() {
        Long categoryId = 3L;

        String expected = "Category was not found by id: " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.deleteCategory(categoryId)
        );

        assertThat(exception.getMessage()).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("""
			Find all categories and sort by given pageable object and return page of categoryResponseDto
			""")
    public void findAllCategories_ByPageable_ReturnPageOfCategoryResponseDto() {
        Category category1 = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        Category category2 = new Category()
                .setId(2L)
                .setName("Comic")
                .setDescription("Funny book");

        CategoryResponseDto categoryResponseDto1 = new CategoryResponseDto()
                .setName("Detective")
                .setId(1L)
                .setDescription("Interesting satisfaction stories about spies and good boys");

        CategoryResponseDto categoryResponseDto2 = new CategoryResponseDto()
                .setName("Comic")
                .setId(2L)
                .setDescription("Funny book");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(List.of(category1, category2), pageable, 2);

        when(categoryMapper.toResponseDto(category1)).thenReturn(categoryResponseDto1);
        when(categoryMapper.toResponseDto(category2)).thenReturn(categoryResponseDto2);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<CategoryResponseDto> categoryResponseDtoPage = categoryService.findAllCategories(pageable);

        assertThat(categoryPage.getContent().size()).isEqualTo(2);
        assertThat(categoryPage.getContent().get(0).getName()).isEqualTo(categoryResponseDtoPage.getContent().get(0).getName());
        verify(categoryMapper).toResponseDto(category1);
        verify(categoryMapper).toResponseDto(category2);
        verify(categoryRepository).findAll(pageable);
        verifyNoMoreInteractions(categoryRepository,  categoryMapper);
    }

    @Test
    @DisplayName("""
			Find all categories and sort by given pageable object and return page of categoryResponseDto
			""")
    public void getCategory_ById_ReturnCategoryResponseDto() {
        Category category = new Category()
                .setId(1L)
                .setName("Detective")
                .setDescription("Interesting satisfaction stories about spies and good boys");

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto()
                .setName("Detective")
                .setId(1L)
                .setDescription("Interesting satisfaction stories about spies and good boys");

        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        CategoryResponseDto actual = categoryService.getCategoryById(category.getId());

        assertThat(actual).isEqualTo(categoryResponseDto);
        verify(categoryMapper).toResponseDto(category);
        verify(categoryRepository).findById(category.getId());
        verifyNoMoreInteractions(categoryRepository,  categoryMapper);
    }

    @Test
    @DisplayName("""
			Get category with invalid id and wait to throw exception
			""")
    public void getCategory_WithInvalidId_ShouldThrowException() {
        Long categoryId = 3L;

        String expected = "Category was not found by id: " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getCategoryById(categoryId)
        );

        assertThat(exception.getMessage()).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
