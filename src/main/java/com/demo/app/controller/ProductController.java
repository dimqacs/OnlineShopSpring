package com.demo.app.controller;

import com.demo.app.model.ProductDTO;
import com.demo.app.model.ResponseDTO;
import com.demo.app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteProductById(@PathVariable final Long id) {
        logger.info("Trying to delete product with id " + id);
        try {
            productService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Product with id " + id + " successfully deleted.", LocalDateTime.now()));
        } catch (EntityNotFoundException e) {
            logger.error("Error while deleting product with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Product with id " + id + " not found.", LocalDateTime.now()));
        } catch (Exception e) {
            logger.error("Error while deleting product with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deletion for product with id " + id + ".", LocalDateTime.now()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody @Validated final ProductDTO productDTO) {
        logger.info("Trying to create product.");
        try {
            productService.createProduct(productDTO);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "Product successfully created.", LocalDateTime.now()));
        } catch (Exception e) {
            logger.error("Can't create product, Error - \n" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't create product, Error - \n" + e, LocalDateTime.now()));
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        logger.info("Trying to send info about all products");
        try {
            return ResponseEntity.ok(productService.findAll());
        } catch (Exception e) {
            logger.error("Error while sending all products, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable final String name) {
        try {
            logger.info("Sent info about product with name " + name);
            return ResponseEntity.ok(productService.findByName(name));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about product with name " + name + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable final Long id) {
        try {
            logger.info("Sent info about product with id " + id);
            return ResponseEntity.ok(productService.findById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about product with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
