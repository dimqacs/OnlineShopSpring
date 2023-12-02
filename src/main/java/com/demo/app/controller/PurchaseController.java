package com.demo.app.controller;

import com.demo.app.domain.Shipper;
import com.demo.app.model.PurchaseDTO;
import com.demo.app.model.UserDTO;
import com.demo.app.service.PurchaseService;
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

@RestController
@RequestMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PurchaseController {

        private final PurchaseService purchaseService;

        private static final Logger logger = LoggerFactory.getLogger(UserController.class);

        @GetMapping("/{id}")
        public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable final Long id){
            try {
                logger.info("Trying to sent info about purchase with id " + id);
                return ResponseEntity.ok(purchaseService.findById(id));
            } catch (ChangeSetPersister.NotFoundException e) {
                logger.error("Error while sending info about purchase with id " + id + ", ERROR - ", e);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updatePurchaseStatus(@PathVariable final Long id , @RequestBody @Validated final PurchaseDTO purchaseDTO){
        try {
            logger.info("Trying to update status for purchase with id " + id);
            purchaseService.update(id, purchaseDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e){
            logger.error("Error while updating status for purchase with id " + id + ", ERROR - ", e);
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
