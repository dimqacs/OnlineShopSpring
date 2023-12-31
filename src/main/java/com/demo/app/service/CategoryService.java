package com.demo.app.service;

import com.demo.app.controller.UserController;
import com.demo.app.domain.Category;
import com.demo.app.domain.User;
import com.demo.app.model.UserDTO;
import com.demo.app.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public void deleteById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            logger.info("Category with id " + id + " successfully deleted.");
        } else {
            logger.error("Can't delete category with id " + id + ".");
            throw new EntityNotFoundException("Product not found with ID: " + id);
        }
    }

    public void create(Category category) {
        Category newCategory = new Category();

        newCategory.setName(category.getName());
        categoryRepository.save(newCategory);

        logger.info("Category with name " + category.getName() + " successfully created.");
    }

    public List<Category> findAll() {
        final List<Category> categories = categoryRepository.findAll(Sort.by("id"));
        logger.info("Info about all categories was successfully send.");
        return categories;
    }
}
