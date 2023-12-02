package com.demo.app.controller;

import com.demo.app.domain.Category;
import com.demo.app.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCategoryById(@PathVariable final Long id){
        try {
            logger.info("Deleting category with id " + id);
            categoryService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while deleting category with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody Category category){
        try {
            logger.info("Trying to create category with name " + category.getName() + ".");
            categoryService.create(category);
            return  ResponseEntity.ok("Category created.");
        } catch (Exception e){
            logger.error("Cant create category with name " + category.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while creating category" + e);
        }
    }

}
