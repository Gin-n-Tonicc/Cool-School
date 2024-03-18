package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.category.CategoryCreateException;
import com.coolSchool.coolSchool.exceptions.category.CategoryNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.entity.Category;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findByDeletedFalse();
        return categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findByIdAndDeletedFalse(id);
        if (category.isPresent()) {
            return modelMapper.map(category.get(), CategoryDTO.class);
        }
        throw new CategoryNotFoundException(messageSource);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        try {
            categoryDTO.setId(null);
            Category categoryEntity = categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
            return modelMapper.map(categoryEntity, CategoryDTO.class);
        } catch (DataIntegrityViolationException exception) {
            // If a category with the same name already exists
            throw new CategoryCreateException(messageSource, true);
        }
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategoryOptional = categoryRepository.findByIdAndDeletedFalse(id);

        if (existingCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(messageSource);
        }

        Category existingCategory = existingCategoryOptional.get();
        modelMapper.map(categoryDTO, existingCategory);

        existingCategory.setId(id);
        Category updatedCategory = categoryRepository.save(existingCategory);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findByIdAndDeletedFalse(id);
        if (category.isPresent()) {
            // Soft delete
            category.get().setDeleted(true);
            categoryRepository.save(category.get());
        } else {
            throw new CategoryNotFoundException(messageSource);
        }
    }
}
