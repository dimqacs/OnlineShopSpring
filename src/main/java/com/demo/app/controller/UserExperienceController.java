package com.demo.app.controller;

import com.demo.app.model.ProductDTO;
import com.demo.app.model.ProductSmallDTO;
import com.demo.app.model.PurchaseRequest;
import com.demo.app.service.ProductService;
import com.demo.app.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ue")
@AllArgsConstructor
@EnableGlobalAuthentication
public class UserExperienceController {

    private final PurchaseService purchaseService;

    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/products")
    public ResponseEntity<List<ProductSmallDTO>> getAllProducts(){
        try {
            logger.info("Sent info about product");
            return ResponseEntity.ok(productService.findAllProducts());
        } catch (Exception e) {
            logger.error("Error while sending info about product, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/purchase/create")
    public ResponseEntity<?> createPurchase(@RequestBody @Validated PurchaseRequest purchaseRequest, Authentication authentication) {
        logger.info("Trying to create a purchase.");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            purchaseService.createPurchase(purchaseRequest, userDetails);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error while creating purchase, ERROR - ", e);
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
