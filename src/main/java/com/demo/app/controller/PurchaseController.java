package com.demo.app.controller;

import com.demo.app.model.PurchaseDTO;
import com.demo.app.model.ResponseDTO;
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

import java.util.List;

@RestController
@RequestMapping(value = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getPurchaseById(@PathVariable final Long id) {
        logger.info("Trying to sent info about purchase with id " + id);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about purchase.", purchaseService.findById(id)));
        } catch (EntityNotFoundException e) {
            logger.error("Error while sending info about purchase with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Purchase with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about purchase with id "+ id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during sending info for purchase with id " + id + ". \n" + e));
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updatePurchaseStatus(@PathVariable final Long id, @RequestBody @Validated final PurchaseDTO purchaseDTO) {
        logger.info("Trying to update status for purchase with id " + id);
        try {
            purchaseService.update(id, purchaseDTO);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Purchase with id " + id + " successfully updated."));
        } catch (EntityNotFoundException e) {
            logger.error("Error while updating status about purchase with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Purchase with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while updating status about purchase with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during updating status for purchase with id " + id + ". \n" + e));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deletePurchaseById(@PathVariable final Long id) {
        logger.info("Trying to delete purchase with id " + id);
        try {
            purchaseService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Purchase with id " + id + " successfully deleted."));
        } catch (EntityNotFoundException e) {
            logger.error("Error while deleting purchase with id " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Purchase with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while deleting purchase with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deleting purchase with id " + id + ". \n" + e));
        }
    }

    @PostMapping("/get/status")
    public ResponseEntity<ResponseDTO> getPurchaseByStatus(@RequestBody @Validated final PurchaseDTO purchaseDTO) {
        logger.info("Trying to give info about all purchases with status " + purchaseDTO.getStatus() + ".");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Purchases with status " + purchaseDTO.getStatus(), purchaseService.findByStatus(purchaseDTO)));
        } catch (Exception e) {
            logger.error("Error while sending info about purchases with status " + purchaseDTO.getStatus(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred sending info about purchases with staus " + purchaseDTO.getStatus() + ". \n" + e));
        }
    }
}
