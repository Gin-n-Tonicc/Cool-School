package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.category.CategoryCreateException;
import com.coolSchool.coolSchool.exceptions.category.CategoryNotFoundException;
import com.coolSchool.coolSchool.exceptions.category.ValidationCategoryException;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.entity.Category;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.services.CategoryService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, Validator validator) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
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
        throw new CategoryNotFoundException();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        try {
            categoryDTO.setId(null);
            Category categoryEntity = categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
            return modelMapper.map(categoryEntity, CategoryDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCategoryException(exception.getConstraintViolations());
        } catch (DataIntegrityViolationException exception) {
            throw new CategoryCreateException(true);
        }
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategoryOptional = categoryRepository.findByIdAndDeletedFalse(id);

        if (existingCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException();
        }

        Category existingCategory = existingCategoryOptional.get();
        modelMapper.map(categoryDTO, existingCategory);

        try {
            existingCategory.setId(id);
            Category updatedCategory = categoryRepository.save(existingCategory);
            return modelMapper.map(updatedCategory, CategoryDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationCategoryException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findByIdAndDeletedFalse(id);
        if (category.isPresent()) {
            category.get().setDeleted(true);
            categoryRepository.save(category.get());
        } else {
            throw new CategoryNotFoundException();
        }
    }
}
