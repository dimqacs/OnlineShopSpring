package com.demo.app.controller;

import com.demo.app.domain.Shipper;
import com.demo.app.model.ResponseDTO;
import com.demo.app.service.ShipperService;
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
@RequestMapping(value = "/shipper", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShipperController {

    private final ShipperService shipperService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getShipperById(@PathVariable final Long id) {
        logger.info("Trying to sent info about shipper with id " + id);
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about shipper.", shipperService.findById(id)));
        } catch (ChangeSetPersister.NotFoundException e) {
            logger.error("Shipper with id " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Shipper with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while sending info about shipper with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about shipper with id " + id + ", ERROR - " + e));
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAllShippers() {
        logger.info("Trying to send info about all shippers.");
        try {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Info about all shippers.", shipperService.findAll()));
        } catch (Exception e) {
            logger.error("Error while sending info about all shippers.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while sending info about all shippers, error - " + e));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteShipperById(@PathVariable final Long id) {
        logger.info("Trying to delete shipper with id " + id);
        try {
            shipperService.deleteById(id);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "Shipper with id " + id + " successfully deleted."));
        } catch (EntityNotFoundException e) {
            logger.error("Shipper with id " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND.value(), "Shipper with id " + id + " not found."));
        } catch (Exception e) {
            logger.error("Error while deleting shipper with id " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred during deletion for shipper with id " + id + "."));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createShipper(@RequestBody Shipper shipper) {
        logger.info("Trying to create a shipper.");
        try {
            shipperService.createShipper(shipper);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.CREATED.value(), "Shipper successfully created."));
        } catch (Exception e) {
            logger.error("Can't create shipper, Error - \n" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "Can't create shipper, Error - \n" + e));
        }
    }
}
