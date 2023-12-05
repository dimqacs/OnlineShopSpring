package com.demo.app.controller;

import com.demo.app.domain.Category;
import com.demo.app.model.ResponseDTO;
import com.demo.app.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/category", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteCategoryById(@PathVariable final Long id) {
        logger.info("Trying to delete category with id " + id);
        try {
            categoryService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Category with id " + id + " successfully deleted."));
        } catch (EntityNotFoundException e) {
            logger.error("Error while deleting category with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Category with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while deleting category with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deletion for category with id " + id + ". \n" + e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createCategory(@RequestBody Category category) {
        logger.info("Trying to create category with name " + category.getName() + ".");
        try {
            categoryService.create(category);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "Category successfully created."));
        } catch (Exception e) {
            logger.error("Can't create category, Error - \n" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't create category, Error - \n" + e));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllCategories() {
        logger.info("Trying to send info about all categories.");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all categories.", categoryService.findAll()));
        } catch (Exception e) {
            logger.error("Error while sending info about all categories, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about all categories, ERROR - ", e));
        }
    }

}
