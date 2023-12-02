package com.demo.app.controller;

import com.demo.app.model.ProductDTO;
import com.demo.app.service.ProductService;
import com.demo.app.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/onlineShop")
@AllArgsConstructor
public class UserRoleController {

    private ProductService productService;

    private final PurchaseService purchaseService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable final Long id){
        try {
            logger.info("Sent info about product with id " + id);
            return ResponseEntity.ok(productService.findById(id));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Error while sending info about product with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Object> createPurchase(@PathVariable final Long id){
        try {
            logger.info("Trying to create purchase from user with id " + id);
            purchaseService.createPurchase(id);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e){
            logger.error("Error while creating purchase from user with id " + id + ", ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
