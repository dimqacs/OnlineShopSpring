package com.demo.app.controller;

import com.demo.app.model.ProductCreateDTO;
import com.demo.app.model.ResponseDTO;
import com.demo.app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteProductById(@PathVariable final Long id) {
        logger.info("Trying to delete product with id " + id);
        try {
            productService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Product with id " + id + " successfully deleted."));
        } catch (EntityNotFoundException e) {
            logger.error("Error while deleting product with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Product with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while deleting product with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deletion for product with id " + id + ". \n" + e));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody @Validated final ProductCreateDTO productCreateDTO) {
        logger.info("Trying to create product.");
        try {
            productService.createProduct(productCreateDTO);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "Product successfully created."));
        } catch (Exception e) {
            logger.error("Can't create product, Error - \n" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't create product, Error - \n" + e));
        }
    }

    @GetMapping()
    public ResponseEntity<ResponseDTO> getAllProducts() {
        logger.info("Trying to send info about all products");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all products.", productService.findAll()));
        } catch (Exception e) {
            logger.error("Error while sending all products, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't sent info about all products, Error - \n" + e));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO> getProductByName(@PathVariable final String name) {
        logger.info("Trying to send info about product with name " + name);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about product.", productService.findByName(name)));
        } catch (EntityNotFoundException e) {
            logger.error("Error while sending info about product with name " + name, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Product with name " + name + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about product with name " + name, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during sending info for product with name " + name + ". \n" + e));
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseDTO> getProductById(@PathVariable final Long id) {
        logger.info("Trying to send info about product with id " + id);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about product.", productService.findById(id)));
        } catch (EntityNotFoundException e) {
            logger.error("Error while sending info about product with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Product with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about product with id "+ id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during sending info for product with id " + id + ". \n" + e));
        }
    }
}
