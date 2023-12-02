package com.demo.app.service;

import com.demo.app.controller.UserController;
import com.demo.app.domain.Category;
import com.demo.app.domain.Product;
import com.demo.app.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public void deleteById(Long id){
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
    }

    public void create(Category category) {
        Category newCategory = new Category();

        newCategory.setName(category.getName());
        categoryRepository.save(newCategory);
        logger.info("Category with name " + category.getName() + " successfully created.");
    }
}
