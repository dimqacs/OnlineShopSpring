package com.demo.app.controller;

import com.demo.app.domain.Shipper;
import com.demo.app.model.PurchaseDTO;
import com.demo.app.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PurchaseController {

        private final PurchaseService purchaseService;

        private static final Logger logger = LoggerFactory.getLogger(UserController.class);

        @GetMapping("/{id}")
        public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable final Long id){
            try {
                logger.info("Sent info about purchase with id " + id);
                return ResponseEntity.ok(purchaseService.findById(id));
            } catch (ChangeSetPersister.NotFoundException e) {
                logger.error("Error while sending info about purchase with id " + id + ", ERROR - ", e);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        }
}
