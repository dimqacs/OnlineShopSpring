package com.demo.app.controller;

import com.demo.app.model.ProductDTO;
import com.demo.app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProductController {

    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable final Long id){
        try {
            logger.info("Deleting product with id " + id);
            productService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while deleting product with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
