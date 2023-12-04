package com.demo.app.controller;

import com.demo.app.model.PurchaseRequest;
import com.demo.app.model.ResponseDTO;
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

@RestController
@RequestMapping("/ue")
@AllArgsConstructor
@EnableGlobalAuthentication
public class UserExperienceController {

    private final PurchaseService purchaseService;

    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO> getAllProducts(){
        logger.info("Trying to send info about all products.");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all products.", productService.findAllProducts()));
        } catch (Exception e) {
            logger.error("Error while sending info about products, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about products, ERROR - " + e));
        }
    }

    @PostMapping("/purchase/create")
    public ResponseEntity<ResponseDTO> createPurchase(@RequestBody @Validated PurchaseRequest purchaseRequest, Authentication authentication) {
        logger.info("Trying to create a purchase.");
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            purchaseService.createPurchase(purchaseRequest, userDetails);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "Purchase successfully created."));
        } catch (Exception e) {
            logger.error("Error while creating purchase, ERROR - ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't create purchase, Error - \n" + e));
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseDTO> getProductById(@PathVariable final Long id) {
        logger.info("Trying to send info about product with id " + id);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about product with id " + id + " .", productService.findById(id)));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Product with id " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Product with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about product with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about product with id " + id + ", ERROR - " + e));
        }
    }
}
